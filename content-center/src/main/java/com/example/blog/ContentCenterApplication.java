package com.example.blog;

import com.example.blog.content.rocketmq.AddBonusSource;
import com.example.blog.content.rocketmq.MySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.example.blog.content.dao")
// 可以不加 @EnableDiscoveryClient
@EnableFeignClients
@EnableBinding({Source.class, MySource.class, AddBonusSource.class})
public class ContentCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentCenterApplication.class, args);
    }

}
