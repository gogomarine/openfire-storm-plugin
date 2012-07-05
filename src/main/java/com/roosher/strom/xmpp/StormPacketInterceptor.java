package com.roosher.strom.xmpp;

import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.Packet;

public class StormPacketInterceptor implements PacketInterceptor{
    
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public void interceptPacket(Packet packet, Session session,
            boolean incoming, boolean processed) throws PacketRejectedException {
        logger.info("packet: {}, session: {}, incoming: {}, processed: {}",
                new Object[]{packet.toString(), session.toString(), incoming, processed});
        
        if (!incoming) {
            return;
        }
        
        if (processed) {
            return;
        }
        
    }
    
}
