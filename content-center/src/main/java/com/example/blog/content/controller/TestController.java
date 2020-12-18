package com.example.blog.content.controller;

import com.example.blog.common.domain.dto.user.UserDTO;
import com.example.blog.content.rocketmq.MySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author xianpeng.xia
 * on 2020/12/9 下午10:27
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    Source source;

    @Autowired
    MySource mySource;

    @GetMapping("/sentinel-rest-template/{userId}")
    public UserDTO sentinelRestTemplate(@PathVariable Integer userId) {
        UserDTO user = restTemplate.getForObject("http://user-center/user/{userId}", UserDTO.class, userId);
        return user;
    }

    @GetMapping("/stream")
    public String streamTest() {
        Message<String> message = MessageBuilder.withPayload("消息体").build();
        boolean result = source.output().send(message);
        return "success";
    }


    @GetMapping("/my/stream")
    public String myStreamTest() {
        Message<String> message = MessageBuilder.withPayload("Hello~~~").build();
        boolean result = mySource.output().send(message);
        return "success";
    }
}
