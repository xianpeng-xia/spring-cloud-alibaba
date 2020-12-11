package com.example.user.rocketmq;

import com.example.common.domain.dto.message.UserAddBonusMsg;
import com.example.user.dao.user.UserMapper;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2020/12/11
 * @time 下午8:13
 */
@Service
@RocketMQMessageListener(consumerGroup = "consumer-group", topic = "add-bonus")
public class AddBonusListener implements RocketMQListener<UserAddBonusMsg> {

    @Autowired
    UserMapper userMapper;

    @Override
    public void onMessage(UserAddBonusMsg userAddBonusMsg) {

    }
}
