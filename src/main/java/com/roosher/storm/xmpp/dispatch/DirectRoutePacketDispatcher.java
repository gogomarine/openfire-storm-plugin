package com.roosher.storm.xmpp.dispatch;

import org.jivesoftware.openfire.IQRouter;
import org.jivesoftware.openfire.MessageRouter;
import org.jivesoftware.openfire.PresenceRouter;
import org.jivesoftware.openfire.XMPPServer;
import org.xmpp.packet.IQ;
import org.xmpp.packet.Message;
import org.xmpp.packet.Presence;

/**
 * 直接依赖openfire内部组件进行消息传输
 * @author gogo
 *
 */
public class DirectRoutePacketDispatcher extends AbstractPacketDispatcher{
    
    private MessageRouter messageRouter;
    
    private PresenceRouter presenceRouter;
    
    private IQRouter iqRouter;
    
    public DirectRoutePacketDispatcher() {
        XMPPServer xmppServer = XMPPServer.getInstance();
        
        this.messageRouter = xmppServer.getMessageRouter();
        this.presenceRouter = xmppServer.getPresenceRouter();
        this.iqRouter = xmppServer.getIQRouter();
    }

    @Override
    public void process(Message message) {
        messageRouter.route(message);
    }
    
    @Override
    public void process(IQ iq) {
        iqRouter.route(iq);
    }
    
    @Override
    public void process(Presence presence) {
        presenceRouter.route(presence);
    }
    
}
