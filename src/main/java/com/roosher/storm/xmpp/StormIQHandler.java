package com.roosher.storm.xmpp;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.jivesoftware.openfire.IQHandlerInfo;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.handler.IQHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.IQ;

import com.roosher.storm.xmpp.cache.BlockListCache;

/**
 * 处理我们自定义的IQ，进行黑名单取消
 * @author gogo
 *
 */
public class StormIQHandler extends IQHandler{
    
    private static final String IQ_BLOCKEE = "blockee";

    private static final String IQ_BLOCKER = "blocker";

    private static final String IQ_TYPE = "type";

    private static final String ITEM = "item";

    private static final String QUERY = "query";

    private final static Logger logger = LoggerFactory.getLogger(StormIQHandler.class);
    
    private IQHandlerInfo iqHandlerInfo;
    
    public StormIQHandler() {
        super("storm");
        iqHandlerInfo = new IQHandlerInfo(QUERY, "storm:iq:blocklist");        
    }
    
    @Override
    public IQ handleIQ(IQ packet) throws UnauthorizedException {
//        logger.info("handle iq packet: {}", packet);
        
        Element element = packet.getElement();
        Element notify = element.element(QUERY);
        Element cache = notify.element(ITEM);
        
        if (cache == null) {
            logger.warn("没有cache 请求");
            return null;
        }
        
        String type = getAttribute(cache, IQ_TYPE);
        String blocker = getAttribute(cache, IQ_BLOCKER);
        String blockee = getAttribute(cache, IQ_BLOCKEE);
        
        // 取消某个人的黑名单
        BlockListCache blockListCache = BlockListCache.getInstance();
        if (StringUtils.equals(type, "remove")) {
//            logger.info("打算取消 :{} 对 {} 的黑名单", blocker, blockee);
            //直接取消, 这里应该还存储数据库
            blockListCache.cacheBlocked(blocker, blockee, false);
            
        } else if (StringUtils.equals(type, "create")) {// 建立黑名单需要
//            logger.info("打算添加 :{} 对 {} 的黑名单", blocker, blockee);
            //直接取消， 这里应该还存储数据库
            blockListCache.cacheBlocked(blocker, blockee, true);
        } else {//查询
            logger.info("现在是否有 {} 对 {} 的黑名单? {}", new Object[]{blocker, blockee, 
                    blockListCache.isBlocked(blocker, blockee)});
        }
        
        return null;//如果这里不返回空，则是希望给个回复回去。这里我们不需要，只需要单方面的交互即可。
    }
    
    private String getAttribute(Element element, String attr) {
        Attribute attribute = element.attribute(attr);
        if (attribute != null) {
            return attribute.getValue();
        } else {
            return StringUtils.EMPTY;
        }
    }

    @Override
    public IQHandlerInfo getInfo() {
        return iqHandlerInfo;
    }
    
}