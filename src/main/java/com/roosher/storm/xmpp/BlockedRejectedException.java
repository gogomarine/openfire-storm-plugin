package com.roosher.storm.xmpp;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.xmpp.packet.JID;

/**
 * 被黑名单屏蔽而被拒绝发送的包异常
 * @author gogo
 *
 */
public class BlockedRejectedException extends PacketRejectedException{

    /**
     * 
     */
    private static final long serialVersionUID = 8677301339389394359L;
    
    private JID blockedJID;
    
    private JID blockerJID;

    public BlockedRejectedException() {
        super();
    }

    public BlockedRejectedException(String msg) {
        super(msg);
    }
    
    public BlockedRejectedException(JID blockedJID, JID blockerJID) {
        super();
        this.blockedJID = blockedJID;
        this.blockerJID = blockerJID;
    }

    public BlockedRejectedException(JID blockedJID, JID blockerJID, String msg) {
        this(msg);
        this.blockedJID = blockedJID;
        this.blockerJID = blockerJID;
    }

    public JID getBlockedJID() {
        return blockedJID;
    }
    
    public JID getBlockerJID() {
        return blockerJID;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("blocked", blockedJID).append("blocker", blockedJID).toString();
    }
    
}
