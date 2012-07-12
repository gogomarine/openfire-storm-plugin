package com.roosher.storm.xmpp;

import java.util.Iterator;

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
import org.xmpp.packet.PacketError;
import org.xmpp.packet.Roster;
import org.xmpp.packet.Roster.Item;

import com.roosher.storm.xmpp.blocklist.BlockList;
import com.roosher.storm.xmpp.blocklist.DatabaseBlockList;
import com.roosher.storm.xmpp.dispatch.DirectRoutePacketDispatcher;
import com.roosher.storm.xmpp.dispatch.PacketDispatcher;
import com.roosher.storm.xmpp.lifecycle.OnStartup;
import com.roosher.storm.xmpp.lifecycle.OnTerminal;

/**
 * 黑名单 拦截器，这里实现核心判断业务, 关于xmpp定义，查询
 * http://tools.ietf.org/id/draft-ietf-xmpp-3921bis-20.html#rfc.section.2.3.3
 * 
 * @author gogo
 *
 */
public class BlockListPacketInterceptor implements PacketInterceptor, OnStartup, OnTerminal{
    
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    private BlockList blockList;
    
    /**
     * violation notification messages will be from this JID
     */
    private JID notificationFrom;
    
    private PacketDispatcher packetDispatcher;
    
    public BlockListPacketInterceptor() {
        blockList = new DatabaseBlockList();
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
//        logger.info("packet: {}, session: {}, incoming: {}, processed: {}",
//                new Object[]{packet, session, incoming, processed});
        

        if(!isValidTargetPacket(packet, incoming, processed)) {
            return;
        }
        
//        logger.info("本次的发包符合需要屏蔽的条件，我们会一一过滤 packet: {}, session: {}, incoming: {}, processed: {}",
//                new Object[]{packet.toString(), session.toString(), incoming, processed});
        
        PacketRejectedException blockedException = null;
        
        JID from = packet.getFrom();
        
        if (packet instanceof Message) {
            JID targetJid = packet.getTo();
            if (blockList.isBlocked(targetJid, from)) {
                blockedException = new PacketRejectedException("People in blocked list won't receive message");
            }
        } else if (packet instanceof Roster) {
            Roster roster = (Roster) packet;
            if (needCheckBlocked(roster)) {//如果是删除的话，不检查是否为好友
                Item item = getRosterItem(roster);
                JID targetJid = null;
                if (item != null && (targetJid = item.getJID()) != null) {
                    if (blockList.isBlocked(targetJid, from)) {
                        blockedException = new PacketRejectedException("People in blocked list won't allow add again");
                        
                        if (session != null) {
                            IQ reply = getBlockedIQ(session, roster);
                            session.process(reply);
                        }
                    }
                }
            }
        }
        
        if (blockedException == null) {
            return;//没有发现任何异常，也表示目前流程不需要再处理了
        }
        
//        sendBlockNotification(from, "System Notification", String.format("You can't [%s] interactive, coz you're in his Blocklist", 
//                targetJid.getNode()));
        
        throw blockedException;
    }
    
    /**
     * 我们只过滤需要检查的 Roster 有可能客户端 传错了参数、比如说多添加了，因为加人只有一个参数，参数多了，是会报错的 查阅错误节点:
     * http://tools.ietf.org/id/draft-ietf-xmpp-3921bis-20.html#roster-add-errors
     * 目前测试过，如果客户端不正确处理删除联系人时，还是会让两个陌生人发送信息
     * 
     * @param roster
     * @return
     */
    protected boolean needCheckBlocked(Roster roster) {
        //有可能客户端 传错了参数、比如说多添加了，因为加人只有一个参数，参数多了，是会报错的
        //查阅错误节点: http://tools.ietf.org/id/draft-ietf-xmpp-3921bis-20.html#roster-add-errors
        //目前测试过，如果客户端不正确处理删除联系人时，还是会让两个陌生人发送信息
        if (roster.getType() == IQ.Type.set) {
            Item item = getRosterItem(roster);
            if (item == null) {
                return false;
            }
            JID targetJID = item.getJID();
            if (targetJID == null) {
                return false;
            }
            
            if (item.getSubscription() != Roster.Subscription.remove) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 获取roster附带索引信息
     * @param roster
     * @param index
     * @return
     */
    private Item getRosterItem(Roster roster, int index) {
        if (roster.getItems() != null && !roster.getItems().isEmpty()) {
            int i = 0;
            for (Iterator<Item> iterator = roster.getItems().iterator(); iterator.hasNext();) {
                Item item = iterator.next();
                if (i == index) {
                    return item;
                } else {
                    i++;//continue
                }
            }
        }
        
        return null;
    }
    
    /**
     * 获取roster，默认从第一个
     * @param roster
     * @return
     */
    private Item getRosterItem(Roster roster) {
        return getRosterItem(roster, 0);
    }

    protected IQ getBlockedIQ(Session session, Roster roster) {
        // An interceptor rejected this packet so answer a not_allowed error
        IQ reply = new IQ();
        reply.setID(roster.getID());
        reply.setTo(session.getAddress());
        reply.setFrom(roster.getTo());
        reply.setError(PacketError.Condition.forbidden);
        logger.info("send blocked iq reply: {}", reply.toString());
        return reply;
    }
    
    /**
     * 判断是否需要进行黑名单拦截判断
     * @param packet
     * @param read
     * @param processed
     * @return
     */
    private boolean isValidTargetPacket(Packet packet, boolean read,
            boolean processed) {
        boolean valid = !processed && read;
        
        if (!valid) {//如果非法，直接返回
            return false;
        }
        
        if (!(packet instanceof Roster) && !(packet instanceof Message)) {
            return false;
        }
        
        return true;//默认都是合法的
    }
    
    @SuppressWarnings("unused")
    private void sendBlockNotification(JID blocked, String subject, String body) {
        Message message = new Message();
        message.setTo(blocked);
        message.setFrom(notificationFrom);
        message.setSubject(subject);
        message.setBody(body);
        
        message.setType(Message.Type.normal);//
        // TODO consider spining off a separate thread here,
        // in high volume situations, it will result in
        // in faster response and notification is not required
        // to be real time.
        packetDispatcher.dispatch(message);
    }
    
}
