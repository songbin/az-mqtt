package com.azq.mqtt.serivce.state;

import com.azq.mqtt.domain.mqtt.ContextBo;
import com.azq.mqtt.serivce.network.SendManager;
import io.netty.handler.codec.mqtt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * client和topic的映射关系管理
 *
 * @author songbin
 * @version Id: ClientManager.java, v 0.1 2019/1/17   Exp $$
 */
public class ClientManager {
    private static final Logger logger = LoggerFactory.getLogger(SessionManger.class);
    /**
     * 存储主题下面包含的所有客户端
     * key topic value上下文关系 包括clientId
     * value不适用map<ContextBo, String>的原因是，订阅时可能Qos改变，所以每次订阅都需要重新设置
     * key=>topic
     * value=>clientId，client网络上下文
     */
    private static Map<String, Map<String, ContextBo>> topicMap = new HashMap<>();
    /**存储一个客户端订阅的主题详情
     * client和topic关联
     * key=>clientId
     * value=>topic列表
     * */
    private static Map<String, Set<String>> clientTopicMap = new HashMap<>();

    /**
     * 将client的上下文相关信息添加到映射关系表中
     */
    public static void addClient(String topic, ContextBo contextBo) {
        try {
            Map<String, ContextBo> clientMap = topicMap.get(topic);
            if (CollectionUtils.isEmpty(clientMap)) {
                clientMap = new HashMap<>();
            }
            clientMap.put(contextBo.getClientId(), contextBo);
            topicMap.put(topic, clientMap);

            /**存储一个客户端订阅的主题详情*/
            Set<String> topicSet = clientTopicMap.get(contextBo.getClientId());
            if(CollectionUtils.isEmpty(topicSet)){
                topicSet = new HashSet<>();
                clientTopicMap.put(contextBo.getClientId(), topicSet);
            }
            topicSet.add(topic);

        } catch (Exception e) {
            logger.warn("异常", e);
        }
    }

    /**
     * 将消息发送到指定topic下的所有client上去
     */
    public static void pubTopic(MqttPublishMessage msg) {
        Map<String, ContextBo> clientMap = topicMap.get(msg.variableHeader().topicName());
        if(CollectionUtils.isEmpty(clientMap)){
            return;
        }
        for(ContextBo contextBo:clientMap.values()){
            SendManager.pubMsg(msg, contextBo);
        }
    }

    /**将client从topic移除*/
    public static void removeClientByTopic(String clientId, String topic){

        Set<String> topicSet = clientTopicMap.get(clientId);
        if(!CollectionUtils.isEmpty(topicSet)){
            topicSet.remove(topic);
        }


        /**找出这个主题下所有客户端*/
        Map<String, ContextBo> clientMap = topicMap.get(topic);
        /**移除该客户端*/
        if(CollectionUtils.isEmpty(clientMap) ||
         !clientMap.containsKey(clientId)){
            return;
        }
        clientMap.remove(clientId);


    }



}
