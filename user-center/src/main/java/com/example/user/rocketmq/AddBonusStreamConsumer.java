package com.example.user.rocketmq;

import com.alibaba.fastjson.JSON;
import com.example.common.domain.dto.message.UserAddBonusMsg;
import com.example.user.dao.user.BonusEventLogMapper;
import com.example.user.dao.user.UserMapper;
import com.example.user.domain.entity.user.BonusEventLog;
import com.example.user.domain.entity.user.User;
import com.example.user.service.UserService;
import java.util.Date;
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
