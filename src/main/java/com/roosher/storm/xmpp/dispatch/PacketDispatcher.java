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
     * 发布xmpp包，默认采用异步机制发送
     * @param iq
     */
    void dispatch(Packet packet);
    
    /**
     * 分发xmpp包，添加是否异步的选项
     * @param packet
     * @param async 是否采用异步发送
     */
    void dispatch(Packet packet, boolean async);
    
}
