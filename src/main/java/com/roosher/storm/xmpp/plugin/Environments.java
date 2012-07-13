package com.roosher.storm.xmpp.plugin;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jivesoftware.util.JiveGlobals;

/**
 * 环境配置
 * @author gogo
 *
 */
public enum Environments {
    
    INSTANCE;
    
    public static Environments getInstance() {
        return INSTANCE;
    }

    /**
     * 是否开启 黑名单过滤功能
     */
    public static final String BLOCKLIST_FILTER_ENABLED_PROPERTY = "plugin.blocklist.filter.enabled";
    
    /**
     * 是否开启 启用openfire本身的缓存功能
     */
    public static final String BLOCKLIST_OPENFIRE_CACHE_ENABLED_PROPERTY = "plugin.blocklist.openfire.cache.enabled";
    
    /**
     * 是否开启 日志查看(查看协议包的日志)功能，如果这个为false ，则下面子集不管是什么，都会被禁止
     */
    public static final String BLOCKLIST_LOG_ENABLED_PROPERTY = "plugin.blocklist.log.enabled";
    
    /**
     * 是否开启 日志查看Message消息协议功能
     */
    public static final String BLOCKLIST_LOG_MESSAGE_ENABLED_PROPERTY = "plugin.blocklist.log.message.enabled";
    
    /**
     * 是否开启 日志查看presence消息协议功能
     */
    public static final String BLOCKLIST_LOG_PRESENCE_ENABLED_PROPERTY = "plugin.blocklist.log.presence.enabled";
    
    /**
     * 是否开启 日志查看IQ协议功能
     */
    public static final String BLOCKLIST_LOG_IQ_ENABLED_PROPERTY = "plugin.blocklist.log.iq.enabled";
    
    /**
     * 是否开启 日志查看非三方包的协议
     */
    public static final String BLOCKLIST_LOG_OTHER_ENABLED_PROPERTY = "plugin.blocklist.log.other.enabled";
    
    /**
     * 是否查看 从客户端上传的日志
     */
    public static final String BLOCKLIST_LOG_INCOMING_ENABLED_PROPERTY = "plugin.blocklist.log.incoming.enabled";

    /**
     * 是否查看 从服务器返回结果的日志
     */
    public static final String BLOCKLIST_LOG_OUTTER_ENABLED_PROPERTY = "plugin.blocklist.log.outter.enabled";
    
    /**
     * 是否查看 已经处理过的日志
     */
    public static final String BLOCKLIST_LOG_PROCESSED_ENABLED_PROPERTY = "plugin.blocklist.log.processed.enabled";
    
    /**
     * 是否查看 还没有处理的Packet的日志
     */
    public static final String BLOCKLIST_LOG_PROCESSING_ENABLED_PROPERTY = "plugin.blocklist.log.processing.enabled";
    
    private boolean blocklistFilterEnabled;
    private boolean blocklistOpenfireCacheEnabled;
    private boolean logMonitorEnabled;
    private boolean logMonitorMessageEnabled;
    private boolean logMonitorPresenceEnabled;
    private boolean logMonitorIQEnabled;
    private boolean logMonitorOtherEnabled;
    
    private boolean logMonitorIncomingEnabled;
    private boolean logMonitorOutterEnabled;
    
    private boolean logMonitorProcessingEnabled;
    private boolean logMonitorProcessedEnabled;
    
    private Environments() {
    }
    
    public boolean isBlocklistFilterEnabled() {
        return blocklistFilterEnabled;
    }

    public void setBlocklistFilterEnabled(boolean enabled) {
        this.blocklistFilterEnabled = enabled;
        JiveGlobals.setProperty(BLOCKLIST_FILTER_ENABLED_PROPERTY, String.valueOf(enabled));
    }

    public boolean isBlocklistOpenfireCacheEnabled() {
        return blocklistOpenfireCacheEnabled;
    }

    public void setBlocklistOpenfireCacheEnabled(
            boolean enabled) {
        this.blocklistOpenfireCacheEnabled = enabled;
        JiveGlobals.setProperty(BLOCKLIST_OPENFIRE_CACHE_ENABLED_PROPERTY, String.valueOf(enabled));
    }

    public boolean isLogMonitorEnabled() {
        return logMonitorEnabled;
    }

    public void setLogMonitorEnabled(boolean enabled) {
        this.logMonitorEnabled = enabled;
        JiveGlobals.setProperty(BLOCKLIST_LOG_ENABLED_PROPERTY, String.valueOf(enabled));
    }

    public boolean isLogMonitorMessageEnabled() {
        return logMonitorMessageEnabled;
    }

    public void setLogMonitorMessageEnabled(boolean enabled) {
        this.logMonitorMessageEnabled = enabled;
        JiveGlobals.setProperty(BLOCKLIST_LOG_MESSAGE_ENABLED_PROPERTY, String.valueOf(enabled));
    }

    public boolean isLogMonitorPresenceEnabled() {
        return logMonitorPresenceEnabled;
    }

    public void setLogMonitorPresenceEnabled(boolean enabled) {
        this.logMonitorPresenceEnabled = enabled;
        JiveGlobals.setProperty(BLOCKLIST_LOG_PRESENCE_ENABLED_PROPERTY, String.valueOf(enabled));
    }

    public boolean isLogMonitorIQEnabled() {
        return logMonitorIQEnabled;
    }

    public void setLogMonitorIQEnabled(boolean enabled) {
        this.logMonitorIQEnabled = enabled;
        JiveGlobals.setProperty(BLOCKLIST_LOG_IQ_ENABLED_PROPERTY, String.valueOf(enabled));
    }
    
    public boolean isLogMonitorOtherEnabled() {
        return logMonitorOtherEnabled;
    }
    
    public void setLogMonitorOtherEnabled(boolean enabled) {
        this.logMonitorOtherEnabled = enabled;
        JiveGlobals.setProperty(BLOCKLIST_LOG_OTHER_ENABLED_PROPERTY, String.valueOf(enabled));
    }
    
    public boolean isLogMonitorIncomingEnabled() {
        return logMonitorIncomingEnabled;
    }

    public void setLogMonitorIncomingEnabled(boolean enabled) {
        this.logMonitorIncomingEnabled = enabled;
        JiveGlobals.setProperty(BLOCKLIST_LOG_INCOMING_ENABLED_PROPERTY, String.valueOf(enabled));
    }

    public boolean isLogMonitorOutterEnabled() {
        return logMonitorOutterEnabled;
    }

    public void setLogMonitorOutterEnabled(boolean enabled) {
        this.logMonitorOutterEnabled = enabled;
        JiveGlobals.setProperty(BLOCKLIST_LOG_OUTTER_ENABLED_PROPERTY, String.valueOf(enabled));
    }

    public boolean isLogMonitorProcessingEnabled() {
        return logMonitorProcessingEnabled;
    }

    public void setLogMonitorProcessingEnabled(boolean enabled) {
        this.logMonitorProcessingEnabled = enabled;
        JiveGlobals.setProperty(BLOCKLIST_LOG_PROCESSING_ENABLED_PROPERTY, String.valueOf(enabled));
    }

    public boolean isLogMonitorProcessedEnabled() {
        return logMonitorProcessedEnabled;
    }

    public void setLogMonitorProcessedEnabled(boolean enabled) {
        this.logMonitorProcessedEnabled = enabled;
        JiveGlobals.setProperty(BLOCKLIST_LOG_PROCESSED_ENABLED_PROPERTY, String.valueOf(enabled));
    }

    /**
     * 从class文件中重置， 重置到系统初始化状态
     */
    public void reset() {
        setBlocklistFilterEnabled(true);
        setBlocklistOpenfireCacheEnabled(true);
        
        setLogMonitorEnabled(false);
        setLogMonitorIQEnabled(false);
        setLogMonitorMessageEnabled(false);
        setLogMonitorPresenceEnabled(false);
        setLogMonitorOtherEnabled(false);//默认其他的包也不需要检查
        
        setLogMonitorIncomingEnabled(true);
        setLogMonitorOutterEnabled(false);//默认查看从客户端上来的日志
        setLogMonitorProcessingEnabled(true);
        setLogMonitorProcessedEnabled(false);//默认查看还没有处理的日志，处理过就不用看了
    }
    
    /**
     * 重新从全局(数据库 或者 当前的openfire 系统中)中读取配置，
     */
    public void reload() {
        blocklistFilterEnabled = JiveGlobals.getBooleanProperty(BLOCKLIST_FILTER_ENABLED_PROPERTY, true);
        blocklistOpenfireCacheEnabled = JiveGlobals.getBooleanProperty(BLOCKLIST_OPENFIRE_CACHE_ENABLED_PROPERTY, true);
        logMonitorEnabled = JiveGlobals.getBooleanProperty(BLOCKLIST_LOG_ENABLED_PROPERTY, false);//默認不開啓日誌
        logMonitorIQEnabled = JiveGlobals.getBooleanProperty(BLOCKLIST_LOG_IQ_ENABLED_PROPERTY, false);
        logMonitorMessageEnabled = JiveGlobals.getBooleanProperty(BLOCKLIST_LOG_MESSAGE_ENABLED_PROPERTY, false);
        logMonitorPresenceEnabled = JiveGlobals.getBooleanProperty(BLOCKLIST_LOG_PRESENCE_ENABLED_PROPERTY, false);
        logMonitorOtherEnabled = JiveGlobals.getBooleanProperty(BLOCKLIST_LOG_OTHER_ENABLED_PROPERTY, false);
        
        logMonitorIncomingEnabled = JiveGlobals.getBooleanProperty(BLOCKLIST_LOG_INCOMING_ENABLED_PROPERTY, true);
        logMonitorOutterEnabled = JiveGlobals.getBooleanProperty(BLOCKLIST_LOG_OUTTER_ENABLED_PROPERTY, false);
        logMonitorProcessingEnabled = JiveGlobals.getBooleanProperty(BLOCKLIST_LOG_PROCESSING_ENABLED_PROPERTY, true);
        logMonitorProcessedEnabled = JiveGlobals.getBooleanProperty(BLOCKLIST_LOG_PROCESSED_ENABLED_PROPERTY, false);
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
    }
}
