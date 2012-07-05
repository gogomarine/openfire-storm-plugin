package com.roosher.strom.xmpp.blacklist;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.JID;

/**
 * 实现黑名单接口，进行一层浅的封装，根据用户id查询是否是黑名单(这个是在这儿逻辑
 * 目前的fox，就是我们JID的node，跟普通xmpp不一样)
 * @author gogo
 *
 */
public abstract class AbstractBlockList implements BlockList{
    
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
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
        
        if (StringUtils.isBlank(username) || !StringUtils.isNumeric(username)) {
            logger.debug("源用户的JID Node节点为空(非数字)，所以直接返回");
            return false;
        }
        
        String contactNode = newContact.getNode();
        if (StringUtils.isBlank(contactNode) || !StringUtils.isNumeric(contactNode)) {
            logger.debug("新加用户的JID Node节点为空(非数字)，所以直接返回");
            return false;
        }
        
        //这里只能用于确认了 节点是int型的
        int sourceId = Integer.parseInt(username);
        int contactId = Integer.parseInt(contactNode);

        boolean blocked = isBlocked(sourceId, contactId);
        logger.debug(String.format("新添加的用户: [%s] 是否已经被 目标用户: [%s] 屏蔽: [%s]", 
                sourceId, contactId, blocked));
        return blocked;
    }
    
    public abstract boolean isBlocked(int sourceId, int newContactId);

}
