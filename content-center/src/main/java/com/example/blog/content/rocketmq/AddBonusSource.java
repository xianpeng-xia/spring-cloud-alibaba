package com.example.blog.content.rocketmq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author xianpeng.xia
 * on 2020/12/13 下午11:25
 */
public interface AddBonusSource {

    String ADD_BONUS_OUTPUT = "add-bonus-output";

    @Output(ADD_BONUS_OUTPUT)
    MessageChannel output();

}
