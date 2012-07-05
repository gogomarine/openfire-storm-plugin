package com.roosher.strom.xmpp;

import java.util.Map;

import org.jivesoftware.util.PropertyEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StormPropertyEventListener implements PropertyEventListener{

    private static final Logger logger = LoggerFactory.getLogger(StormPropertyEventListener.class);
    
    @Override
    public void propertySet(String property, Map<String, Object> params) {
        logger.debug("property set : {}, params: #{}", property, params);
    }

    @Override
    public void propertyDeleted(String property, Map<String, Object> params) {
        logger.debug("property delete : {}, params: #{}", property, params);
    }

    @Override
    public void xmlPropertySet(String property, Map<String, Object> params) {
        logger.debug("property xml set : {}, params: #{}", property, params);
    }

    @Override
    public void xmlPropertyDeleted(String property, Map<String, Object> params) {
        logger.debug("property xml delete : {}, params: #{}", property, params);
    }

    
    
}
