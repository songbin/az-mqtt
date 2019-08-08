package com.azq.mqtt.serivce.protocol;

import com.azq.mqtt.domain.mqtt.ContextBo;
import com.azq.mqtt.serivce.protocol.resp.PubCompResp;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**PUBREL报文是对PUBREC报文的响应。它是QoS 2等级协议交换的第三个报文。
 * @author songbin
 * @version Id: PubRelHandler.java, v 0.1 2019/1/16   Exp $$
 */
public class PubRelHandler {
    private static final Logger logger = LoggerFactory.getLogger(PubRelHandler.class);
    public static void onPubRel(ContextBo contextBo, MqttMessage msg) {
        logger.debug("客户端发布释放:{}", msg);
        PubCompResp.sendPubComp(contextBo, msg);
    }
}
