package com.roosher.storm.xmpp;

import org.jivesoftware.whack.ExternalComponentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.component.Component;
import org.xmpp.component.ComponentException;
import org.xmpp.component.ComponentManager;
import org.xmpp.packet.JID;
import org.xmpp.packet.Packet;


public class StormComponent implements Component{
    
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    ComponentManager componentManager;
    
    public StormComponent() {
        initialize();
    }
    
    private void initialize() {
        ExternalComponentManager manager =  StormExternalComponent.getExternalComponent();
        manager.setConnectTimeout(30);
        manager.setSecretKey("storm", "storm-component");
        manager.setMultipleAllowed("storm", false);
        
        try {
            manager.addComponent("storm", this);
        } catch (ComponentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDescription() {
        return "storm test component";
    }
    
    @Override
    public String getName() {
        return "storm-Component";
    }
    
    @Override
    public void initialize(JID jid, ComponentManager componentManager)
            throws ComponentException {
        System.out.println("jid is " + jid.toString());
        System.out.println("component manager :" + componentManager);
        
        this.componentManager = componentManager;
    }
    
    @Override
    public void processPacket(Packet packet) {
        logger.debug("get packet from openfire: {}", packet);
    }
    
    public void sendPacket(Packet packet) {
        logger.debug("send packet to openfire: {}", packet);
        
        try {
            componentManager.sendPacket(this, packet);
        } catch (ComponentException e) {
            logger.error("can't send packet to openfire: {}, may be openfire is down!" + packet, e);
        }
    }
    
    @Override
    public void shutdown() {
        try {
            componentManager.removeComponent("storm");
        } catch (ComponentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        System.out.println("start storm component");
    }
    
}
