package com.example.blog.content.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @date 2020/12/19
 * @time 下午12:40
 */
@RestController
@RequestMapping("/nacos/config")
@RefreshScope
public class ConfigController {

    @Value("${your.configuration}")
    public String yourConfiguration;

    @GetMapping("/get")
    public String yourConfiguration() {
        return yourConfiguration;
    }
}
