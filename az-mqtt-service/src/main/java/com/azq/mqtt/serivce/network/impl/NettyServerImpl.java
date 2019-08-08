package com.azq.mqtt.serivce.network.impl;

import com.azq.mqtt.common.constants.SysParamConstant;
import com.azq.mqtt.serivce.network.NettyServer;
import com.azq.mqtt.serivce.core.MqttHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author songbin
 * @version Id: NettyServerImpl.java, v 0.1 2019/1/15   Exp $$
 */
@Service
@Slf4j
public class NettyServerImpl  implements NettyServer {


    @Override
    public void start() throws Exception {
        ServerBootstrap b = new ServerBootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        b.group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch)
                            throws Exception {
                        log.info("initChannel ch:{}", ch);
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new MqttDecoder());
                        p.addLast(MqttEncoder.INSTANCE);
                        p.addLast(new MqttHandler());
                    }


                })
                .option(ChannelOption.SO_BACKLOG, 128) // determining the number of connections queued
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);

        b.bind(SysParamConstant.SERVER_PORT).sync();

    }


}
