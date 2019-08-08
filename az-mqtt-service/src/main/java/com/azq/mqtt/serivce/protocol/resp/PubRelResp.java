package com.azq.mqtt.serivce.protocol.resp;

import com.azq.mqtt.domain.mqtt.ContextBo;
import com.azq.mqtt.serivce.network.SendManager;
import io.netty.handler.codec.mqtt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**PUBREC报文是对QoS等级2的PUBLISH报文的响应。它是QoS 2等级协议交换的第二个报文
 * @author songbin
 * @version Id: PubRecHandler.java, v 0.1 2019/1/16   Exp $$
 */

public class PubRelResp {
    private static final Logger logger = LoggerFactory.getLogger(PubRelResp.class);
    public static void sendPubRel(ContextBo contextBo, MqttMessage msg) {


        MqttQoS mqttQoS = msg.fixedHeader().qosLevel();
        logger.debug("准备向客户端发送rel");
        MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.PUBREL, false, MqttQoS.AT_LEAST_ONCE, false, 2);;

        // 不是级别最高的QOS 返回 puback 即可
       // MqttMessageIdVariableHeader msgIdVariableHeader = MqttMessageIdVariableHeader.from(msgId);
        MqttMessage ackMessage = new MqttMessage(fixedHeader, msg.variableHeader());
        SendManager.responseMsg(contextBo, ackMessage, null, true);
    }

}
