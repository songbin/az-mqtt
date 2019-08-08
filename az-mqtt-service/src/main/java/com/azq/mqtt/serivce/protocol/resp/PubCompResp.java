package com.azq.mqtt.serivce.protocol.resp;

import com.azq.mqtt.domain.mqtt.ContextBo;
import com.azq.mqtt.serivce.network.SendManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**PUBCOMP报文是对PUBREL报文的响应。它是QoS 2等级协议交换的第四个也是最后一个报文。
 * @author songbin
 * @version Id: PubCompHandler.java, v 0.1 2019/1/16   Exp $$
 */
public class PubCompResp {
    private static final Logger logger = LoggerFactory.getLogger(PubCompResp.class);


    public static void sendPubComp(ContextBo contextBo, MqttMessage msg) {
        logger.debug("准备向客户端发送compete");

        MqttQoS mqttQoS = msg.fixedHeader().qosLevel();
        MqttFixedHeader fixedHeader = null;

        // 不是级别最高的QOS 返回 puback 即可
        fixedHeader = new MqttFixedHeader(MqttMessageType.PUBCOMP, false, mqttQoS, false, 1);
        MqttMessage ackMessage = new MqttMessage(fixedHeader, msg.variableHeader());
        SendManager.responseMsg(contextBo, ackMessage, null, true);
    }
}
