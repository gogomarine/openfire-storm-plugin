package com.roosher.strom.xmpp;

import org.jivesoftware.openfire.IQHandlerInfo;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.handler.IQHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.IQ;

public class StormIQHandler extends IQHandler{
    
    private static final Logger logger = LoggerFactory.getLogger(StormIQHandler.class);
    
    private IQHandlerInfo iqHandlerInfo;
    
    public StormIQHandler() {
        super("halo");
        iqHandlerInfo = new IQHandlerInfo("halo", "halo-ns");        
    }
    
    @Override
    public IQ handleIQ(IQ packet) throws UnauthorizedException {
        
        logger.debug("IQ packet: {}", packet.toString());
        
        return packet;
    }

    @Override
    public IQHandlerInfo getInfo() {
        return iqHandlerInfo;
    }
    
}
