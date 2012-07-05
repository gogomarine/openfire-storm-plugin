package com.roosher.storm.xmpp.plugin;

import java.io.File;

import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.roster.RosterEventDispatcher;
import org.jivesoftware.util.PropertyEventDispatcher;

import com.roosher.storm.xmpp.BlockListPacketInterceptor;
import com.roosher.storm.xmpp.StormIQHandler;
import com.roosher.storm.xmpp.StormPropertyEventListener;
import com.roosher.storm.xmpp.StormRosterListener;

/**
 * 
 * @author gogo
 *
 */
public class StormPlugin implements Plugin{
    
//    private static final Logger logger = LoggerFactory.getLogger(StormPlugin.class);
    
    private StormPropertyEventListener haloPropertyEventListener;
    private StormIQHandler debugIQHandler;
    private BlockListPacketInterceptor blockListPacketInterceptor;
    
    private InterceptorManager interceptorManager;
    private StormRosterListener rosterListener;
    
    public StormPlugin() {
        haloPropertyEventListener = new StormPropertyEventListener();
        debugIQHandler = new StormIQHandler();
        blockListPacketInterceptor = new BlockListPacketInterceptor();
        
        interceptorManager = InterceptorManager.getInstance();
        
        rosterListener = new StormRosterListener();
    }

    @Override
    public void initializePlugin(PluginManager manager, File pluginDirectory) {
        XMPPServer xmppServer = XMPPServer.getInstance();
        xmppServer.getIQRouter().addHandler(debugIQHandler);
        PropertyEventDispatcher.addListener(haloPropertyEventListener);
        
        blockListPacketInterceptor.onStartup();
        
        interceptorManager.addInterceptor(blockListPacketInterceptor);
        
        RosterEventDispatcher.addListener(rosterListener);
    }

    @Override
    public void destroyPlugin() {
        
        XMPPServer xmppServer = XMPPServer.getInstance();
        xmppServer.getIQRouter().removeHandler(debugIQHandler);
        
        PropertyEventDispatcher.removeListener(haloPropertyEventListener);

        interceptorManager.removeInterceptor(blockListPacketInterceptor);
        
        blockListPacketInterceptor.onTerminal();
        
        RosterEventDispatcher.removeListener(rosterListener);
    }
    
    
}
