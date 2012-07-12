package com.roosher.storm.xmpp;

import org.jivesoftware.openfire.roster.Roster;
import org.jivesoftware.openfire.roster.RosterEventListener;
import org.jivesoftware.openfire.roster.RosterItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roosher.storm.xmpp.blocklist.BlockList;
import com.roosher.storm.xmpp.blocklist.DatabaseBlockList;

public class StormRosterListener implements RosterEventListener{
    
    private static final Logger logger = LoggerFactory.getLogger(StormRosterListener.class);
    
    private BlockList blockList;
    
    public StormRosterListener() {
        blockList = new DatabaseBlockList();
    }
    
    public void setBlockList(BlockList blockList) {
        this.blockList = blockList;
    }

    @Override
    public void rosterLoaded(Roster roster) {
        logger.info("loaded roster: {}", roster);
    }

    @Override
    public boolean addingContact(Roster roster, RosterItem item,
            boolean persistent) {
        logger.info("adding contact roster: {} with item: {}, persitent: #{}",
                new Object[]{roster, item, persistent});
        
        boolean blocked = blockList.isBlocked(roster.getUsername(), item.getJid());
        
        //分发 某个人被屏蔽的ofRoster去
        
        return !blocked;//没有被屏蔽才加入
    }

    @Override
    public void contactAdded(Roster roster, RosterItem item) {
        logger.info("contactAdded: {} with item: {}, persitent: #{}",
                new Object[]{roster, item});
    }

    @Override
    public void contactUpdated(Roster roster, RosterItem item) {
        logger.info("contactUpdated: {} with item: {}, persitent: #{}",
                new Object[]{roster, item});
    }

    @Override
    public void contactDeleted(Roster roster, RosterItem item) {
        logger.info("contactDeleted: {} with item: {}, persitent: #{}",
                new Object[]{roster, item});
    }

    
    
}
