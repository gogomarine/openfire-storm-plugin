package com.roosher.storm.xmpp.dispatch;

import org.xmpp.packet.Packet;

import com.roosher.storm.xmpp.lifecycle.OnStartup;
import com.roosher.storm.xmpp.lifecycle.OnTerminal;

/**
 * 
 * @author gogo
 *
 */
public interface PacketDispatcher extends OnStartup, OnTerminal{
    
    /**
     * 
     * @param iq
     */
    void dispatch(Packet packet);
    
}
