package com.roosher.storm.xmpp;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import org.dom4j.Element;
import org.jivesoftware.openfire.session.Session;
import org.junit.Before;
import org.junit.Test;
import org.xmpp.packet.IQ;
import org.xmpp.packet.IQ.Type;
import org.xmpp.packet.JID;
import org.xmpp.packet.Roster.Item;
import org.xmpp.packet.Roster.Subscription;
import org.xmpp.packet.Roster;

import com.google.common.collect.Lists;

public class BlockListPacketInterceptorTest {
    
    BlockListPacketInterceptor interceptor;
    
//    @Before
    public void setUp() {
        interceptor = new BlockListPacketInterceptor();
    }
    
    /**
     * <pre>
     * &ltiq id="vb2fi-56" type="set" from="lady@veriton/Spark 2.6.3">
     *   &ltquery xmlns="jabber:iq:roster">
     *     &ltitem jid="dev@veriton" name="dev">
     *       &ltgroup>Friends&lt/group>
     *     &lt/item>
     *   &lt/query>
     * &lt/iq>
     * &lt/pre>
     */
    public Roster mockRequestRoster() {
        Roster subRequest = new Roster();
        subRequest.setID("vjzfg-13");
        subRequest.setType(Type.set);
        subRequest.setFrom("lady@veriton/Spark 2.6.3");
        
        subRequest.addItem(new JID("dev", "veriton", null), "dev", 
                null, Subscription.none, Lists.newArrayList("Friends"));
        
        return subRequest;
    }
    
    @Test
    public void getGetReplyIQ() {
//        Roster request = mockRequestRoster();
//        System.out.println(request);
//
//        Session session = createMock(Session.class);
//        expect(session.getAddress()).andReturn(new JID("lady@veriton/Spark 2.6.3"));
//        replay(session);
//        IQ blockedIQ = interceptor.getBlockedIQ(session, request);
//        System.out.println(blockedIQ);
    }
    
}
