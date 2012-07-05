package com.roosher.storm.xmpp.blocklist;

import org.xmpp.packet.JID;

/**
 * 黑名单， 提供黑名单关系， 业务级别
 * @author gogo
 *
 */
public interface BlockList {
    
    /**
     * 查询新的联系人  对于 该JID来说，是否是Block的关系
     * @param username
     * @param newContact
     * @return
     */
    boolean isBlocked(String username, JID newContact);
    
    /**
     * 
     * @param source
     * @param newContact
     * @return
     */
    boolean isBlocked(JID source, JID newContact);
    
    /**
     * 主动屏蔽掉某个人
     * @param source
     * @param blockedContact
     * @return
     */
    boolean block(String username, JID blockedContact);
    
}
