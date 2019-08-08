package com.azq.mqtt.serivce.protocol.resp;

import com.azq.mqtt.domain.mqtt.ContextBo;
import com.azq.mqtt.serivce.network.SendManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**PUBACK报文是对QoS 1等级的PUBLISH报文的响应。
 * @author songbin
 * @version Id: PublishBackHandler.java, v 0.1 2019/1/16   Exp $$
 */
public class PubAckResp {
    private static final Logger logger = LoggerFactory.getLogger(PubAckResp.class);
    public static void sendPubAck(ContextBo contextBo, MqttPublishMessage msg) {
        logger.debug("向客户端发送puback报文");
        MqttQoS mqttQoS = msg.fixedHeader().qosLevel();
        MqttFixedHeader fixedHeader = null;

        // 不是级别最高的QOS 返回 puback 即可
        fixedHeader = new MqttFixedHeader(MqttMessageType.PUBACK, false, mqttQoS, false, 0);

        int msgId = msg.variableHeader().messageId();
        MqttMessageIdVariableHeader msgIdVariableHeader = MqttMessageIdVariableHeader.from(msgId);
        MqttPubAckMessage ackMessage = new MqttPubAckMessage(fixedHeader, msgIdVariableHeader);
        SendManager.responseMsg(contextBo, ackMessage, msgId, true);

    }

}
