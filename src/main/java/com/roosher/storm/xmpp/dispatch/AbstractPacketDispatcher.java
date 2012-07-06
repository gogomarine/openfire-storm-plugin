package com.roosher.storm.xmpp.dispatch;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.IQ;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;
import org.xmpp.packet.Presence;

/**
 * 抽象的xmpp包的分发器
 * @author gogo
 *
 */
public abstract class AbstractPacketDispatcher implements PacketDispatcher{
    
    protected Logger logger = LoggerFactory.getLogger(getClass());
    
    private static ExecutorService dispatchService;
    
    private final static ThreadFactory dispatchFactory = new ThreadFactory() {
        
        private AtomicInteger threadCount = new AtomicInteger();
        
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("Storm-Packet-Dispatcher-" + threadCount.getAndIncrement());
            t.setPriority(Thread.MIN_PRIORITY);
            return t;
        }
    };
    
    public AbstractPacketDispatcher() {
        initilize();
    }
    
    @Override
    public void dispatch(Packet packet) {
        dispatch(packet, true);
    }
    
    @Override
    public void dispatch(Packet packet, boolean async) {
        if (async) {
            dispatchService.submit(new DispachTask(packet));
        } else {
            doProcessPacket(packet);
        }
    }
    
    private void initilize() {
        if (dispatchService == null) {
            //XXX 换成自己实现的线程池，自己持有 BlockedQueueLinkedList的引用，在结束的时候
            //进行dump，避免丢失信息，并且在开始的时候，进行读取dump信息
            dispatchService = Executors.newFixedThreadPool(2, dispatchFactory);
        }
    }
    
    @Override
    public void onStartup() {
        synchronized (this) {
            initilize();
        }
    }
    
    @Override
    public void onTerminal() {
        synchronized (this) {
            try {
                dispatchService.awaitTermination(5l, TimeUnit.SECONDS);
                
                dispatchService = null;
            } catch (InterruptedException e) {
                //XXX 暂时不用做什么事情，除非我们有非常强烈的需求 不能丢失这种系统级别的信息
            }
        }
    }
    
    class DispachTask implements Callable<Void> {
        private Packet packet;
        
        public DispachTask(Packet packet) {
            this.packet = packet;
        }
        
        @Override
        public Void call() throws Exception {
            doProcessPacket(packet);
            return null;
        }
    }
    
    /**
     * 真正的处理xmpp包
     * @param packet
     */
    protected void doProcessPacket(Packet packet) {
        if (packet instanceof IQ) {
            process((IQ) packet);
        } else if (packet instanceof Message) {
            process((Message) packet);
        } else if (packet instanceof Presence) {
            process((Presence) packet);
        } else {
            //no idea 
            throw new IllegalAccessError("can't support packet type: " + packet.getClass().getName());
        }
    }
    
    /**
     * 处理IQ包
     * @param iq
     */
    protected void process(IQ iq) {}
    
    /**
     * 处理消息包
     * @param message
     */
    protected void process(Message message) {}
    
    /**
     * 处理 在线信息
     * @param presence
     */
    protected void process(Presence presence) {}
    
}
