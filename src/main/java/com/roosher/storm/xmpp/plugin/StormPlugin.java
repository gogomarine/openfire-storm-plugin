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
import com.roosher.storm.xmpp.cache.BlockListCache;

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
    
    private BlockListCache blockListCache;
    
    public StormPlugin() {
        haloPropertyEventListener = new StormPropertyEventListener();
        debugIQHandler = new StormIQHandler();
        blockListPacketInterceptor = new BlockListPacketInterceptor();
        
        interceptorManager = InterceptorManager.getInstance();
        
        rosterListener = new StormRosterListener();
        
        blockListCache = BlockListCache.getInstance();
    }

    @Override
    public void initializePlugin(PluginManager manager, File pluginDirectory) {
        
        blockListCache.onStartup();
        
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

        // 先于转发完目前的任务，然后再移除黑名单。
        blockListPacketInterceptor.onTerminal();
        
        interceptorManager.removeInterceptor(blockListPacketInterceptor);
        
        RosterEventDispatcher.removeListener(rosterListener);
        
        blockListCache.onTerminal();
    }
    
    
}
