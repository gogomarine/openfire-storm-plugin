package com.roosher.storm.xmpp;

import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;
import org.xmpp.packet.Roster;
import org.xmpp.packet.Roster.Item;

import com.roosher.storm.xmpp.blocklist.BlockList;
import com.roosher.storm.xmpp.blocklist.RemoteBlockList;
import com.roosher.storm.xmpp.dispatch.DirectRoutePacketDispatcher;
import com.roosher.storm.xmpp.dispatch.PacketDispatcher;
import com.roosher.storm.xmpp.lifecycle.OnStartup;
import com.roosher.storm.xmpp.lifecycle.OnTerminal;

public class BlockListPacketInterceptor implements PacketInterceptor, OnStartup, OnTerminal{
    
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    private BlockList blockList;
    
    /**
     * violation notification messages will be from this JID
     */
    private JID notificationFrom;
    
    private PacketDispatcher packetDispatcher;
    
    public BlockListPacketInterceptor() {
        blockList = new RemoteBlockList();
        notificationFrom = new JID(XMPPServer.getInstance().getServerInfo().getXMPPDomain());
        packetDispatcher = new DirectRoutePacketDispatcher();
    }
    
    public void setBlockList(BlockList blockList) {
        this.blockList = blockList;
    }
    
    @Override
    public void onStartup() {
        packetDispatcher.onStartup();
    }
    
    @Override
    public void onTerminal() {
        packetDispatcher.onTerminal();
    }
    
    @Override
    public void interceptPacket(Packet packet, Session session,
            boolean incoming, boolean processed) throws PacketRejectedException {
        logger.info("packet: {}, session: {}, incoming: {}, processed: {}",
                new Object[]{packet.toString(), session.toString(), incoming, processed});
        
        if(!isValidTargetPacket(packet, incoming, processed)) {
            return;
        }
        
        logger.info("本次的发包符合需要屏蔽的条件，我们会一一过滤 packet: {}, session: {}, incoming: {}, processed: {}",
                new Object[]{packet.toString(), session.toString(), incoming, processed});
        
        BlockedRejectedException blockedException = null;
        
        JID from = packet.getFrom();
        
        if (packet instanceof Message) {
            JID targetJid = packet.getTo();
            if (blockList.isBlocked(targetJid, from)) {
                blockedException = new BlockedRejectedException(from, targetJid,"People in blocked list won't receive message");
            }
        } else if (packet instanceof Roster) {
            Roster roster = (Roster) packet;
            if (roster.getType() == IQ.Type.set) {
                for (Item item : roster.getItems()) {
                    JID contactToBeAdd = item.getJID();
                    if (blockList.isBlocked(contactToBeAdd, from)) {
                        blockedException = new BlockedRejectedException(from, contactToBeAdd, "People in blocked list won't allow add again");
                        break;
                    }
                }
            }
        }
        
        if (blockedException != null) {
            sendBlockNotification(from, "系统提示", String.format("你无法跟 ID是: [%s]的人进行操作，因为你已经在TA的黑名单.", 
                    blockedException.getBlockerJID().getNode()));
            
            throw blockedException;
        }
    }
    
    private boolean isValidTargetPacket(Packet packet, boolean read,
            boolean processed) {
        return !processed
                && read
                && ((packet instanceof Roster));
    }
    
    private void sendBlockNotification(JID blocked, String subject, String body) {
        Message message = new Message();
        message.setTo(blocked);
        message.setFrom(notificationFrom);
        message.setSubject(subject);
        message.setBody(body);
        
        message.setType(Message.Type.chat);
        // TODO consider spining off a separate thread here,
        // in high volume situations, it will result in
        // in faster response and notification is not required
        // to be real time.
        packetDispatcher.dispatch(message);
    }
    
}
