package com.roosher.storm.xmpp.plugin;

import java.io.File;

import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.roster.RosterEventDispatcher;
import org.jivesoftware.util.PropertyEventDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roosher.strom.xmpp.StormIQHandler;
import com.roosher.strom.xmpp.StormPropertyEventListener;
import com.roosher.strom.xmpp.StormRosterListener;
import com.roosher.strom.xmpp.StormPacketInterceptor;

/**
 * 
 * @author gogo
 *
 */
public class StormPlugin implements Plugin{
    
    private static final Logger logger = LoggerFactory.getLogger(StormPlugin.class);
    
    private StormPropertyEventListener haloPropertyEventListener;
    private StormIQHandler debugIQHandler;
    private StormPacketInterceptor debugPacketInterceptor;
    
    private InterceptorManager interceptorManager;
    private StormRosterListener rosterListener;
    
    public StormPlugin() {
        haloPropertyEventListener = new StormPropertyEventListener();
        debugIQHandler = new StormIQHandler();
        debugPacketInterceptor = new StormPacketInterceptor();
        
        interceptorManager = InterceptorManager.getInstance();
        
        rosterListener = new StormRosterListener();
    }

    @Override
    public void initializePlugin(PluginManager manager, File pluginDirectory) {
        XMPPServer xmppServer = XMPPServer.getInstance();
        xmppServer.getIQRouter().addHandler(debugIQHandler);
        PropertyEventDispatcher.addListener(haloPropertyEventListener);
        
        interceptorManager.addInterceptor(debugPacketInterceptor);
        
        RosterEventDispatcher.addListener(rosterListener);
    }

    @Override
    public void destroyPlugin() {
        
        XMPPServer xmppServer = XMPPServer.getInstance();
        xmppServer.getIQRouter().removeHandler(debugIQHandler);
        
        PropertyEventDispatcher.removeListener(haloPropertyEventListener);
        
        interceptorManager.removeInterceptor(debugPacketInterceptor);
        
        RosterEventDispatcher.removeListener(rosterListener);
    }
    
    
}
