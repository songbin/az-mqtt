package com.azq.mqtt.serivce.protocol;

import com.azq.mqtt.domain.mqtt.ContextBo;
import com.azq.mqtt.serivce.protocol.resp.PubAckResp;
import com.azq.mqtt.serivce.protocol.resp.PubRecResp;
import com.azq.mqtt.serivce.state.ClientManager;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author songbin
 * @version Id: PublishHandle.java, v 0.1 2019/1/16   Exp $$
 */
public class PublishHandler {
    private static final Logger logger = LoggerFactory.getLogger(PublishHandler.class);

    public static void onPublish(ContextBo contextBo, MqttPublishMessage msg) {
        try{
            logger.debug("接收到publish请求:{}", msg);
            MqttQoS mqttQoS = msg.fixedHeader().qosLevel();
             switch (mqttQoS){
                 case AT_MOST_ONCE:
                     /**qos=0 不需要回复*/
                     break;
                 case AT_LEAST_ONCE:
                     PubAckResp.sendPubAck(contextBo, msg);
                     break;
                 case EXACTLY_ONCE:
                     PubRecResp.sendPubRec(contextBo, msg);
                     break;
                 case FAILURE:
                     break;
             }
            sendMsgToClient(msg);
        }catch (Exception e){
            logger.warn("发消息异常:{}",e);
        }

    }


    /**
     * 将publish接收到的消息主动推送到该topic下所有终端上去
     * */
    private static void sendMsgToClient(MqttPublishMessage msg){
        try{
            ClientManager.pubTopic(msg);
        }catch (Exception e){
            logger.warn("发消息异常:{}",e);
        }
    }
}
