package com.roosher.storm.xmpp.blocklist;

/**
 * 基于远程的(rpc或者..api等)实现的黑名单
 * @author gogo
 *
 */
public class RemoteBlockList extends AbstractBlockList {
    
    @Override
    public boolean isBlocked(String sourceId, String newContactId) {
        return true;
    }

}
