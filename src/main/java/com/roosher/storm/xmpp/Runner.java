package com.roosher.storm.xmpp;

import org.xmpp.packet.IQ;
import org.xmpp.packet.IQ.Type;

public class Runner {
    public static void main(String[] args) {
        StormComponent component = new StormComponent();
        component.start();
        
        IQ create = new IQ(Type.set);
        
        create.setFrom("storm.veriton");
        create.setTo("veriton");
        
        create.setChildElement("query", "storm:iq:blocklist")
                .addElement("item").addAttribute("blocker", "dev")
                .addAttribute("blockee", "lady").addAttribute("type", "create");
        
        component.sendPacket(create);
        //=============== query
        IQ query = new IQ(Type.get);
        query.setChildElement("query", "storm:iq:blocklist")
                .addElement("item").addAttribute("blocker", "dev")
                .addAttribute("blockee", "lady").addAttribute("type", "query");
        component.sendPacket(query);
        
        //=============== query over. start to remove . 
        
        IQ remove = new IQ(Type.set);
        
        remove.setFrom("storm.veriton");
        remove.setTo("veriton");
        
        remove.setChildElement("query", "storm:iq:blocklist")
                .addElement("item").addAttribute("blocker", "dev")
                .addAttribute("blockee", "lady").addAttribute("type", "remove");
        
        component.sendPacket(remove);
        
        //query again
        component.sendPacket(query);
        
        //then add 
        component.sendPacket(create);
        
        //query again
        component.sendPacket(query);
        
        component.shutdown();
        
        try {
            Thread.currentThread().join(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        while(true) {
//            try {
//                Thread.currentThread().join();
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
    }
}
