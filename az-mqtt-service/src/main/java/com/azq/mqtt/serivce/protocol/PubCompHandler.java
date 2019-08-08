package com.azq.mqtt.serivce.protocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**PUBCOMP报文是对PUBREL报文的响应。它是QoS 2等级协议交换的第四个也是最后一个报文。
 * @author songbin
 * @version Id: PubCompHandler.java, v 0.1 2019/1/16   Exp $$
 */
@Service
@Slf4j
public class PubCompHandler {
    private static final Logger logger = LoggerFactory.getLogger(PubCompHandler.class);
    public static void onPubComp(ChannelHandlerContext ctx, MqttPublishMessage msg) {
        logger.debug("客户端发布完成:{}", msg);

//
//        MqttQoS mqttQoS = msg.fixedHeader().qosLevel();
//        MqttFixedHeader fixedHeader = null;
//        // 否则发送发布收到 QOS级别会2
//        fixedHeader = new MqttFixedHeader(MqttMessageType.PUBCOMP, false, MqttQoS.EXACTLY_ONCE, false, 0);
//
//        int msgId = msg.variableHeader().messageId();
//        MqttMessageIdVariableHeader msgIdVariableHeader = MqttMessageIdVariableHeader.from(msgId);
//
//        MqttPubAckMessage ackMessage = new MqttPubAckMessage(fixedHeader, msgIdVariableHeader);
//        ctx.write(ackMessage);
    }
}
