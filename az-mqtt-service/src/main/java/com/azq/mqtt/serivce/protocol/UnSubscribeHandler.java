package com.azq.mqtt.serivce.protocol;

import com.azq.mqtt.domain.mqtt.ContextBo;
import com.azq.mqtt.serivce.network.SendManager;
import com.azq.mqtt.serivce.state.ClientManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author songbin
 * @version Id: UnSubcribeHandler.java, v 0.1 2019/1/16   Exp $$
 */
public class UnSubscribeHandler {
    private static final Logger logger = LoggerFactory.getLogger(UnSubscribeHandler.class);
    public static void onUnsubscribe(ContextBo contextBo, MqttUnsubscribeMessage msg) {
        logger.debug("接收到Unsubscribe请求{}", msg);
       List<String > topics = msg.payload().topics();
        MqttSubAckPayload mqttSubAckPayload = new MqttSubAckPayload(MqttQoS.AT_MOST_ONCE.value());

        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(
                MqttMessageType.UNSUBACK,
                false,
                MqttQoS.AT_MOST_ONCE,
                false,
                0
        );
        MqttMessageIdVariableHeader mqttMessageIdVariableHeader = MqttMessageIdVariableHeader.from(msg.variableHeader().messageId());
        MqttUnsubAckMessage mqttSubAckMessage = (MqttUnsubAckMessage)MqttMessageFactory.newMessage(
                mqttFixedHeader, mqttMessageIdVariableHeader, mqttSubAckPayload);
        SendManager.responseMsg(contextBo, mqttSubAckMessage, null, true);

        if(!CollectionUtils.isEmpty(topics)){
            for(String topic:topics){
                ClientManager.removeClientByTopic(contextBo.getClientId(), topic);
            }
        }

    }
}
