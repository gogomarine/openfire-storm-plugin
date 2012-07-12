package com.roosher.storm.util;


/**
 * <pre>
 * // sample usage
 * public static void main(String[] args) {
 *     StopWatch s = new StopWatch();
 *     s.start();
 *     // code you want to time goes here
 *     s.stop();
 *     System.out.println(&quot;elapsed time in milliseconds: &quot; + s.getElapsedTime());
 * 
 *     // 如果我希望在一个方法中，持续的看某些步骤消耗了多少毫秒，这个方法就是用来做这个的。
 *     // 比如说我
 *     fetchUserFromRemote("url"); 
 *     System.out.println("time costs: " + s.getLatestElapsed());//A
 *     fetchImageFromRemote("url");
 *     System.out.println("time costs: " + s.getLatestElapsed());//B，这个打印从 A到B一共消耗的毫秒数
 *     fetchMusicFromRemote("url");
 *     System.out.println("time costs: " + s.getLatestElapsed());//C，这个打印从 B到C一共消耗的毫秒数
 *     
 * }
 * </pre>
 * 
 * @author gogo
 * 
 */
public class StopWatch {
    private long startTime = 0;
    private long stopTime = 0;
    private boolean running = false;
    
    private long lastChecked = 0;//最后一次检查的时间
    
    public StopWatch() {
        start();
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
        this.running = true;
    }

    public void stop() {
        this.stopTime = System.currentTimeMillis();
        this.running = false;
    }

    // elaspsed time in milliseconds
    public long getElapsedTime() {
        long elapsed;
        if (running) {
            elapsed = (System.currentTimeMillis() - startTime);
        } else {
            elapsed = (stopTime - startTime);
        }
        return elapsed;
    }

    // elaspsed time in seconds
    public long getElapsedTimeSecs() {
        long elapsed;
        if (running) {
            elapsed = ((System.currentTimeMillis() - startTime) / 1000);
        } else {
            elapsed = ((stopTime - startTime) / 1000);
        }
        return elapsed;
    }
    
    /**
     * 建议一般用这个方法，可以调试某个步骤消耗的时间，因为会重置起始时间
     * @return
     */
    public long getLatestElapsed() {
        if (lastChecked == 0) {
            long elapsed = getElapsedTime();
            lastChecked = System.currentTimeMillis();
            return elapsed;
        } else {
            long now = System.currentTimeMillis();
            long elapsed = now - lastChecked;
            lastChecked = now;
            return elapsed;
        }
    }
}