package com.azq.mqtt.aplication;

import com.azq.mqtt.common.constants.SysParamConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

/**
 * @author songbin
 * @version Id: AZQApplication.java, v 0.1 2019/1/14    Exp $$
 */
@Configuration
@SpringBootApplication(scanBasePackages = {"com.azq.mqtt"})
@Slf4j
public class AZQApplication {

    public static void main(String[] args){
        parseCommand(args);
        SpringApplication.run(AZQApplication.class, args);
        log.info("应用程序启动成功，MQTT协议监听端口:{}", SysParamConstant.SERVER_PORT);
    }

    private static void parseCommand(String[] args){
        try{
            final Options options = new Options();
            final Option option = new Option("p", true, "listen port");
            final Option optionHelp = new Option("h", false, "list all command");
            options.addOption(option);
            options.addOption(optionHelp);

            final CommandLineParser parser = new PosixParser();
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("h")) {
                System.out.println(options.toString());
                System.exit(0);
            }else if(cmd.hasOption("p")){
                SysParamConstant.SERVER_PORT = Integer.parseInt(cmd.getOptionValue("p"));
            }

        }catch (Exception e){
            //log.info("解析参数异常", e);
        }

    }




}
