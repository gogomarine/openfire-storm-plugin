package com.roosher.storm.xmpp.blocklist;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.JID;

import com.roosher.storm.util.StopWatch;
import com.roosher.storm.xmpp.cache.BlockListCache;
import com.roosher.storm.xmpp.plugin.Environments;

/**
 * 实现黑名单接口，进行一层浅的封装，根据用户id查询是否是黑名单(这个是在这儿逻辑
 * 目前的fox，就是我们JID的node，跟普通xmpp不一样)
 * @author gogo
 *
 */
public abstract class AbstractBlockList implements BlockList{
    
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    protected BlockListCache blockListCache;
    
    protected Environments environments;
    
    public AbstractBlockList() {
        blockListCache = BlockListCache.getInstance();
        environments = Environments.getInstance();
    }
    
    @Override
    public boolean block(String username, JID blockedContact) {
        throw new Error("need to be implemented");
    }
    
    @Override
    public boolean isBlocked(String username, JID newContact) {
        if (username == null) {
            throw new IllegalArgumentException("source jid can't be null");
        }
        
        if (newContact == null) {
            throw new IllegalArgumentException("new contact jid can't be null");
        }
        
        if (StringUtils.isBlank(username)) {
//            logger.debug("源用户的JID Node节点为空，所以直接返回");
            return false;
        }
        
        String contactNode = newContact.getNode();
        if (StringUtils.isBlank(contactNode)) {
//            logger.debug("新加用户的JID Node节点为空(非数字)，所以直接返回");
            return false;
        }
        
        StopWatch watch = null;
        if (logger.isDebugEnabled()) {
            watch = new StopWatch();
        }
        
        boolean blocked = checkBlocked(username, contactNode);
        if (watch != null) {
            logger.debug("判断: [{}] 是否在 [{}] 的黑名单中，一共花费了: {}(ms)", 
                    new Object[]{contactNode, username, watch.getElapsedTime()});
            watch = null;
        }
//        logger.debug(String.format("新添加的用户: [%s] 是否已经被 目标用户: [%s] 屏蔽: [%s]", 
//                contactNode, username, blocked));
        return blocked;
    }

    /**
     * 使用各种方式判断是否在黑名单, 里面依赖缓存
     * 测试不使用缓存的前提下，通过纯数据库走，需要1~7ms，这个是2个客户端链接上的。一台空闲
     * 数据库的数据，在生产环境下可能更加多。
     * 使用内存是0~1(ms)，其中偏0ms 占到了99%以上
     * @param username
     * @param contactNode
     * @return
     */
    private boolean checkBlocked(String username, String contactNode) {
        boolean useCache = environments.isBlocklistOpenfireCacheEnabled();
        
        if (useCache) {
            Boolean cachedBlock = blockListCache.isBlocked(username, contactNode);
            if (cachedBlock != null) {//因为同步的问题(可能刚好到期)
                return cachedBlock.booleanValue();
            }
        }
        //表示缓存没有捕获到
        boolean blocked = isBlocked(username, contactNode);
        
        if (useCache) {
            blockListCache.cacheBlocked(username, contactNode, blocked);
        }
        
        return blocked;
    }
    
    @Override
    public boolean isBlocked(JID source, JID newContact) {
        if (source == null) {
            throw new IllegalArgumentException("source jid can't be null");
        }
        
        return isBlocked(source.getNode(), newContact);
    }
    
    /**
     * 查看黑名单
     * @param sourceId 参数已经经过过滤，不可能为空
     * @param newContactId 参数已经经过过滤，不可能为空
     * @return 是否 newContactId 在 sourceId 的黑名单中
     */
    public abstract boolean isBlocked(String sourceId, String newContactId);

}
