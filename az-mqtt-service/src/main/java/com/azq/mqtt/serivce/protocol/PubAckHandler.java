package com.azq.mqtt.serivce.protocol;

import com.azq.mqtt.domain.mqtt.ContextBo;
import io.netty.handler.codec.mqtt.MqttPubAckMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PUBACK报文是对QoS 1等级的PUBLISH报文的响应。
 *
 * @author songbin
 * @version Id: PublishBackHandler.java, v 0.1 2019/1/16   Exp $$
 */

public class PubAckHandler {
    private static final Logger logger = LoggerFactory.getLogger(PubAckHandler.class);

    public static void onPubAck(ContextBo contextBo, MqttPubAckMessage msg) {
        //logger.debug("接收客户端发布确认,即将给客户端返回PubRec");


    }



}


