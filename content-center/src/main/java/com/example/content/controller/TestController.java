package com.example.content.controller;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.alibaba.fastjson.JSON;
import com.example.common.domain.dto.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/sentinel-rest-template/{userId}")
    public UserDTO sentinelRestTemplate(@PathVariable Integer userId) {
        UserDTO user = restTemplate.getForObject("http://user-center/user/{userId}", UserDTO.class, userId);
        return user;
    }
}
