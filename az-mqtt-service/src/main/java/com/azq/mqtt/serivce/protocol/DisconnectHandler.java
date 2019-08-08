package com.azq.mqtt.serivce.protocol;

import com.azq.mqtt.domain.mqtt.ContextBo;
import com.azq.mqtt.serivce.state.SessionManger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author songbin
 * @version Id: DisconnectHandler.java, v 0.1 2019/1/16   Exp $$
 */
public class DisconnectHandler {
    private static final Logger logger = LoggerFactory.getLogger(DisconnectHandler.class);

    public static void onDisconnect(ContextBo contextBo) {
        logger.debug("接收到disconnect请求:{}", contextBo);
        try{
            if(!contextBo.getConnected()){
                contextBo.getHandlerContext().close();
                return;
            }

            SessionManger.disConnect(contextBo.getClientId());
        }catch (Exception e){

        }
//        if (!this.connected) {
//            ctx.close();
//            return;
//        }
//
//        BrokerSessionHelper.removeSession(this.clientId, ctx);
//
//        this.willMessage = null;
//
//        this.connected = false;
//
//        ctx.close();

    }
}
