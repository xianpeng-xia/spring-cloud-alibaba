package com.example.content.rocketmq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @date 2020/12/12
 * @time 下午6:01
 */
public interface MySource {

    String MY_SOURCE = "my-source";

    @Output(MY_SOURCE)
    MessageChannel output();

}
