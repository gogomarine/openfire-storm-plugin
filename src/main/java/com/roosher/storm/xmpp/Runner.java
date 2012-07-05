package com.roosher.storm.xmpp;

public class Runner {
    public static void main(String[] args) {
        new StormComponent();
        
        while(true) {
            try {
                Thread.currentThread().join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
