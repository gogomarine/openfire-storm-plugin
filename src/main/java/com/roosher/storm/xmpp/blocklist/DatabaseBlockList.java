package com.roosher.storm.xmpp.blocklist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseBlockList extends AbstractBlockList{
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseBlockList.class);
    
    public static final String QUERY_BLOCKED =
            "SELECT blockId FROM ofBlocklist WHERE ownerUsername = ? AND withUsername = ?";
    
    @Override
    public boolean isBlocked(String sourceId, String newContactId) {
        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
                
        try {
             connection = DbConnectionManager.getConnection();
             pstm = connection.prepareStatement(QUERY_BLOCKED);
             
             pstm.setString(1, sourceId);
             pstm.setString(2, newContactId);
             
             rs = pstm.executeQuery();
             return rs.next();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        } finally {
            DbConnectionManager.closeConnection(rs, pstm, connection);
        }
        
        return false;
    }
    
}
