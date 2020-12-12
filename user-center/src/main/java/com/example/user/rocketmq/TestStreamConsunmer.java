package com.example.user.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Service;

/**
 * @date 2020/12/12
 * @time 下午5:53
 */
@Service
@Slf4j
public class TestStreamConsunmer {

    @StreamListener(Sink.INPUT)
    public void receive(String messageBody) {
        log.info("Get message : {}", messageBody);
    }
}
