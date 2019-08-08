package com.azq.mqtt.serivce.core;

import com.azq.mqtt.domain.mqtt.ContextBo;
import com.azq.mqtt.serivce.network.SendManager;
import com.azq.mqtt.serivce.protocol.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author songbin
 * @version Id: MqttHandler.java, v 0.1 2019/1/15   Exp $$
 */
public class MqttHandler  extends SimpleChannelInboundHandler<MqttMessage> {
    private static final Logger logger = LoggerFactory.getLogger(MqttHandler.class);
    ContextBo contextBo = new ContextBo();
    @Override
    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    protected void channelRead0(ChannelHandlerContext ctx, MqttMessage msg) throws Exception {
        try{
            //logger.debug("收到消息:{}", msg);
            this.contextBo.setHandlerContext(ctx);
            this.verify(ctx, msg);
            this.dispatch(ctx,msg);
        }catch (Exception e){
            ReferenceCountUtil.release(msg);
        }

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("exceptionCaught", cause);
        if(null != ctx) ctx.close();
    }

    private void dispatch(ChannelHandlerContext ctx, MqttMessage msg){

        switch (msg.fixedHeader().messageType()) {
            case CONNECT:
                //this.updateContext(ctx, (MqttConnectMessage) msg);
                ConnectHandler.onConnect(this.contextBo, (MqttConnectMessage) msg);
                break;
            case PUBLISH:// 客户端发布普通消息
                PublishHandler.onPublish(this.contextBo, (MqttPublishMessage) msg);
                break;
            case PUBACK://客户端发布确认 是对QoS=1的发布报文的响应
                logger.info("收到新消息:客户端发布确认 是对QoS=1的发布报文的响应");
                PubAckHandler.onPubAck(this.contextBo, (MqttPubAckMessage)msg);
                break;
            case PUBREC:// 客户端发布收到 是对QoS=2的发布报文的响应
                PubRecHandler.onPubRec(this.contextBo, msg);
                break;
            case PUBREL:// 客户端发布释放 是对PUBREC的相应
                PubRelHandler.onPubRel(this.contextBo, msg);
                break;
            case PUBCOMP://客户端发布完成 是对PUBREL的相应
                PubCompHandler.onPubComp(ctx, (MqttPublishMessage)msg);
                break;
            case SUBSCRIBE:
                SubscribeHandler.onSubscribe(this.contextBo, (MqttSubscribeMessage) msg);
                break;
            case UNSUBSCRIBE:
                UnSubscribeHandler.onUnsubscribe(this.contextBo, (MqttUnsubscribeMessage) msg);
                break;
            case PINGREQ:
                PingReqHandler.onPingReq(contextBo);
                break;
            case DISCONNECT:
                DisconnectHandler.onDisconnect(contextBo);
                break;
        }
    }

    private void verify(ChannelHandlerContext ctx, MqttMessage msg){
        if (msg.decoderResult().isFailure()) {

            Throwable cause = msg.decoderResult().cause();

            if (cause instanceof MqttUnacceptableProtocolVersionException) {

                SendManager.responseMsg(
                        contextBo,
                        MqttMessageFactory.newMessage(
                                new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                                new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_REFUSED_UNACCEPTABLE_PROTOCOL_VERSION, false),
                                null),
                        null,
                        true);

            } else if (cause instanceof MqttIdentifierRejectedException) {

                SendManager.responseMsg(
                        contextBo,
                        MqttMessageFactory.newMessage(
                                new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                                new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_REFUSED_IDENTIFIER_REJECTED, false),
                                null),
                        null,
                        true);
            }

            ctx.close();

            return;
        }
    }



















}
