package com.azq.mqtt.serivce.protocol;

import com.azq.mqtt.domain.mqtt.ContextBo;
import com.azq.mqtt.serivce.state.ClientManager;
import com.azq.mqtt.serivce.network.SendManager;
import com.azq.mqtt.serivce.state.SessionManger;
import io.netty.handler.codec.mqtt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author songbin
 * @version Id: SubscribeHandler.java, v 0.1 2019/1/16   Exp $$
 */
public class SubscribeHandler {
    private static final Logger logger = LoggerFactory.getLogger(SubscribeHandler.class);
    public static void onSubscribe(ContextBo contextBo, MqttSubscribeMessage msg) {
        logger.debug("新的订阅:{}", msg);
        List<MqttTopicSubscription> topics = msg.payload().topicSubscriptions();
        MqttSubAckPayload mqttSubAckPayload = new MqttSubAckPayload(MqttQoS.AT_MOST_ONCE.value());

       MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(
                MqttMessageType.SUBACK,
                false,
                MqttQoS.AT_MOST_ONCE,
                false,
                0
        );
        MqttMessageIdVariableHeader mqttMessageIdVariableHeader = MqttMessageIdVariableHeader.from(msg.variableHeader().messageId());
        MqttSubAckMessage mqttSubAckMessage = (MqttSubAckMessage)MqttMessageFactory.newMessage(
                mqttFixedHeader, mqttMessageIdVariableHeader, mqttSubAckPayload);
        SendManager.responseMsg(contextBo, mqttSubAckMessage, null, true);

        if(!CollectionUtils.isEmpty(topics)){
            for(MqttTopicSubscription item:topics){
                ClientManager.addClient(item.topicName(), contextBo);
            }

        }


    }
    /**检查是否connect过*/
    private boolean checkSession(String clientId){
        ContextBo contextBo = SessionManger.getContextByClientId(clientId);
        if(null == contextBo){
            return false;
        }
        return true;
    }
}
