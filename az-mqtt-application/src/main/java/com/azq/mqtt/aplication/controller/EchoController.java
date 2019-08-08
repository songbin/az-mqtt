package com.azq.mqtt.aplication.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author songbin
 * @version Id: EchoController.java, v 0.1 2019/1/15   Exp $$
 */
@Slf4j
@RestController
public class EchoController {

    @RequestMapping(value = "/echo", method = RequestMethod.POST)
    public String editAccount(String data) {
        return data;
    }
}
