package com.example.content.configration;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @date 2020/12/04
 * @time 下午3:22
 */
@Component
public class Beans {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
