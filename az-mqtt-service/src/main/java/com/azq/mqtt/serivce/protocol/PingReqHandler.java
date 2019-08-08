package com.azq.mqtt.serivce.protocol;

import com.azq.mqtt.domain.mqtt.ContextBo;
import com.azq.mqtt.serivce.network.SendManager;
import com.fasterxml.jackson.core.JsonEncoding;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author songbin
 * @version Id: PingReqHandler.java, v 0.1 2019/1/16   Exp $$
 */
public class PingReqHandler {
    private static final Logger logger = LoggerFactory.getLogger(PingReqHandler.class);
    public static void onPingReq(ContextBo contextBo) {
        logger.debug("ping...");
        try{
            MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PINGRESP, false, MqttQoS.AT_MOST_ONCE, false, 0);
            MqttMessage mqttMessage = new MqttMessage(mqttFixedHeader);
            //contextBo.getHandlerContext().write(mqttMessage);
            SendManager.sendMessage(mqttMessage, contextBo.getClientId(), null, true);
        }catch (Exception e){
            logger.warn("处理ping消息异常:{}", e);
        }

    }
}
