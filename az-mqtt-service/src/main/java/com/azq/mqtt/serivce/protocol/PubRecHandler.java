package com.azq.mqtt.serivce.protocol;

import com.azq.mqtt.domain.mqtt.ContextBo;
import com.azq.mqtt.serivce.protocol.resp.PubRelResp;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**PUBREC报文是对QoS等级2的PUBLISH报文的响应。它是QoS 2等级协议交换的第二个报文
 * @author songbin
 * @version Id: PubRecHandler.java, v 0.1 2019/1/16   Exp $$
 */
@Slf4j
@Service
public class PubRecHandler {
    public static void onPubRec(ContextBo contextBo, MqttMessage msg) {
        log.debug("接收到来自客户端的REC报文{}",msg);
        PubRelResp.sendPubRel(contextBo, msg);

    }

}
