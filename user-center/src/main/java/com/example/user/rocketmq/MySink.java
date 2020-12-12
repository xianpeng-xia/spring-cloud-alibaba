package com.example.user.rocketmq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @date 2020/12/12
 * @time 下午6:14
 */
public interface MySink {

    String MY_INPUT = "my-input";

    @Input(MY_INPUT)
    SubscribableChannel input();
}
