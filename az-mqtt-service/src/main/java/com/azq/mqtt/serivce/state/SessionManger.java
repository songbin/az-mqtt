package com.azq.mqtt.serivce.state;

import com.azq.mqtt.domain.mqtt.ContextBo;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**网络连接管理
 * clientId和网络链接的映射关系
 * @author songbin
 * @version Id: SessionManger.java, v 0.1 2019/1/17   Exp $$
 */
public class SessionManger {
    private static final Logger logger = LoggerFactory.getLogger(SessionManger.class);
    //存储clientId和网络上下文映射关系
    private static Map<String, ContextBo> contextMap = new HashMap<>();
    /**key 网络链接， value 上下文信息*/
    private static Map<ChannelHandlerContext, ContextBo> ctxMap = new HashMap<>();
    
    /**
     *  当有新合法网络链接请求，使用该方法进行网络上线文关系存储
     *  1.存储上下文和clientId的映射
     *  2.存储上下文和CTX的映射
     * */
    public static void newConnect(String clientId, ContextBo contextBo){
        if(StringUtils.isEmpty(clientId) || !verifyContext(contextBo)){
            logger.warn("newconnect[{}]的上下文有误{}", contextBo, contextBo);
            return;
        }
        contextMap.put(clientId, contextBo);
        ctxMap.put(contextBo.getHandlerContext(), contextBo);
    }
    /**
     *  当client发起disconnect请求，使用该方法更新存储关系 同时关闭相关链接
     * */
    public static void disConnect(String clientId){
        try{
            removeContextByClientId(clientId);
        }catch (Exception e){

        }

    }

    /**
     * ping 连续超时移除链接
     * */
    public static void pingTimeout(String clientId, ContextBo contextBo){
        try{
            removeContextByClientId(clientId);
        }catch (Exception e){

        }
    }

    /**
     * 根据clientId获取上下文网络信息
     * */
    public static ContextBo getContextByClientId(String clientId){
        return contextMap.get(clientId);
    }
    /**
     * 关闭上下文网络
     * */
    public static void removeContextByClientId(String clientId){
        try{
            if(StringUtils.isEmpty(clientId) || !contextMap.containsKey(clientId) ){
                return ;
            }

            ContextBo contextBo = contextMap.get(clientId);
            if(!verifyContext(contextBo)){
                logger.warn("disconnect[{}]的上下文有误{}", contextBo, contextBo);
                return;
            }
            contextBo.getHandlerContext().close();
            contextMap.remove(clientId);
            ctxMap.remove(contextBo.getHandlerContext());
        }catch (Exception e){

        }
    }

    /**
     * 关闭上下文网络
     * */
    public static void removeContextByCtx(ChannelHandlerContext ctx){
        try{
            if(null == ctx || !ctxMap.containsKey(ctx) ){
                return ;
            }

            ContextBo contextBo = contextMap.get(ctx);
            if(!verifyContext(contextBo)){
                logger.warn("disconnect[{}]的上下文有误{}", contextBo, contextBo);
                return;
            }
            contextBo.getHandlerContext().close();
            contextMap.remove(contextBo.getClientId());
            ctxMap.remove(contextBo.getHandlerContext());
        }catch (Exception e){

        }
    }
    private static boolean verifyContext(ContextBo contextBo){
        if(null == contextBo || null == contextBo.getHandlerContext()){
            return false;
        }
        return true;
    }


}
