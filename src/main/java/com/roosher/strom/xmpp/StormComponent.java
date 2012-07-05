package com.roosher.strom.xmpp;

import org.jivesoftware.whack.ExternalComponentManager;
import org.xmpp.component.Component;
import org.xmpp.component.ComponentException;
import org.xmpp.component.ComponentManager;
import org.xmpp.packet.JID;
import org.xmpp.packet.Packet;


public class StormComponent implements Component{
    
    ComponentManager componentManager;
    
    public StormComponent() {
        initialize();
    }
    
    private void initialize() {
        ExternalComponentManager manager =  StormExternalComponent.getExternalComponent();
        manager.setConnectTimeout(30);
        manager.setSecretKey("strom", "strom-component");
        manager.setMultipleAllowed("strom", false);
        
        try {
            manager.addComponent("strom", this);
        } catch (ComponentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDescription() {
        return "strom test component";
    }
    
    @Override
    public String getName() {
        return "strom-Component";
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
        System.out.println(packet.toXML());
    }
    
    @Override
    public void shutdown() {
        try {
            componentManager.removeComponent("halo");
        } catch (ComponentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        System.out.println("start halo component");
    }
    
}
