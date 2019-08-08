package com.azq.mqtt.aplication.init;

import com.azq.mqtt.serivce.network.NettyServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author songbin
 * @version Id: InitApplication.java, v 0.1 2019/1/15   Exp $$
 */
@Component
@Slf4j
public class InitApplication {
    @Autowired
    NettyServer nettyServer;

    @PostConstruct
    public void init(){
        try{
            nettyServer.start();
        }catch (Exception e){
            log.error("启动异常", e);
            System.exit(-1);
        }

    }
}
