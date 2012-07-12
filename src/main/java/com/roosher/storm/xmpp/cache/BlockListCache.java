package com.roosher.storm.xmpp.cache;

import org.jivesoftware.util.cache.Cache;
import org.jivesoftware.util.cache.CacheFactory;

import com.roosher.storm.xmpp.lifecycle.OnStartup;
import com.roosher.storm.xmpp.lifecycle.OnTerminal;

public enum BlockListCache implements OnStartup, OnTerminal{
    
    INSTANCE;
    
    private static final String JOINER = "_$_&_";
    
    static final Byte BLOCKED_BYTE = Byte.valueOf((byte) 1);
    static final Byte NOT_BLOCKED_BYTE = Byte.valueOf((byte)0);
    
    static final byte B = 1;
    
    public static BlockListCache getInstance() {
        return INSTANCE;
    }
    
    private static final String BLOCKLIST_CACHE_KEY = "Block List Cache";
    
    private Cache<String, Byte> blockListCache;
    
    @Override
    public void onTerminal() {
        CacheFactory.destroyCache(BLOCKLIST_CACHE_KEY);//销毁不会回收blockListCache 引用本身，只是清空了
    }
    
    @Override
    public void onStartup() {
        blockListCache = CacheFactory.createCache(BLOCKLIST_CACHE_KEY);
    }
    
    /**
     * 
     * @param blocker
     * @param blockee
     * @return
     */
    public boolean hasCache(String blocker, String blockee) {
        return this.blockListCache.containsKey(getBlockCacheKey(blocker, blockee));
    }
    
    public Boolean isBlocked(String blocker, String blockee) {
        Byte blocked = this.blockListCache.get(getBlockCacheKey(blocker, blockee));
        if (blocked == null) {
            return null;
        } else {
            return blocked.byteValue() == B;//如果是1则是被屏蔽了， 目前false占用5个字节数，byte占用1个
        }
    }
    
    public void cacheBlocked(String blocker, String blockee, boolean blocked) {
        this.blockListCache.put(getBlockCacheKey(blocker, blockee), blocked ? BLOCKED_BYTE : NOT_BLOCKED_BYTE);
    }
    
    /**
     * 根据黑名单拥有者 以及 查询用户 查询缓存key
     * @param blocker
     * @param blockee
     * @return
     */
    private String getBlockCacheKey(String blocker, String blockee) {
        StringBuilder key = new StringBuilder(blocker.length() + blockee.length() + JOINER.length());//5个分割符
        key.append(blocker).append(JOINER).append(blockee);
        return key.toString();
    }
    
}
