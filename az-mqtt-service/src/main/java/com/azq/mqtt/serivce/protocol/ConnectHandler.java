package com.azq.mqtt.serivce.protocol;

import com.azq.mqtt.domain.mqtt.ContextBo;
import com.azq.mqtt.serivce.network.SendManager;
import com.azq.mqtt.serivce.state.SessionManger;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MQTT Connect请求响应
 * @author songbin
 * @version Id: ConnectHandler.java, v 0.1 2019/1/16   Exp $$
 */
public class ConnectHandler {

    private static final Logger logger = LoggerFactory.getLogger(ConnectHandler.class);
    public static void onConnect(ContextBo contextBo, MqttConnectMessage msg) {
        logger.debug("新的链接请求:{}", msg);
        ChannelHandlerContext ctx = contextBo.getHandlerContext();
        MqttVersion version = MqttVersion.fromProtocolNameAndLevel(msg.variableHeader().name(), (byte) msg.variableHeader().version());
        String clientId = msg.payload().clientIdentifier();
        boolean cleanSession = msg.variableHeader().isCleanSession();
        contextBo.setVersion(version);
        contextBo.setClientId(clientId);
        contextBo.setCleanSession(cleanSession);


        if (msg.variableHeader().keepAliveTimeSeconds() > 0 && msg.variableHeader().keepAliveTimeSeconds() <= contextBo.getKeepAliveMax()) {
            int keepAlive = msg.variableHeader().keepAliveTimeSeconds();
            contextBo.setKeepAlive(keepAlive);
        }

        //有可能发送俩次的连接包。如果已经存在连接就是关闭当前的连接。
        if (contextBo.getConnected()) {
            ctx.close();
            return;
        }

        SendManager.responseMsg(
                contextBo,
                MqttMessageFactory.newMessage(
                        new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                        new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_ACCEPTED, !contextBo.getCleanSession()),
                        null),
                null,
                true);

        SessionManger.removeContextByClientId(contextBo.getClientId());



        contextBo.setConnected(true);
        SessionManger.newConnect(contextBo.getClientId(), contextBo);
    }
}
