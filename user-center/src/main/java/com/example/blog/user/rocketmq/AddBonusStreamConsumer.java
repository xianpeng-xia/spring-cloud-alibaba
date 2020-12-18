package com.example.blog.user.rocketmq;

import com.alibaba.fastjson.JSON;
import com.example.blog.common.domain.dto.message.UserAddBonusMsg;
import com.example.blog.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.stereotype.Service;

/**
 * @date 2020/12/12
 * @time 下午6:19
 */
@Service
@Slf4j
public class AddBonusStreamConsumer {

    @Autowired
    UserService userService;

    @StreamListener(AddBonusSink.ADD_BONUS_INPUT)
    public void receive(String msg) {
        UserAddBonusMsg userAddBonusMsg = JSON.parseObject(msg, UserAddBonusMsg.class);
        userService.addBonus(userAddBonusMsg);
        log.info("Add bonus message {}", msg);
    }

    @StreamListener("errorChannel")
    public void error(Message<?> message) {
        ErrorMessage errorMessage = (ErrorMessage) message;
        log.info("Error {}", errorMessage);
    }

}
