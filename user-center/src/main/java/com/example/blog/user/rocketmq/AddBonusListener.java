package com.example.blog.user.rocketmq;

import com.example.blog.common.domain.dto.message.UserAddBonusMsg;
import com.example.blog.user.dao.user.BonusEventLogMapper;
import com.example.blog.user.dao.user.UserMapper;
import com.example.blog.user.domain.entity.user.BonusEventLog;
import com.example.blog.user.domain.entity.user.User;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2020/12/11
 * @time 下午8:13
 */
@Service
@Slf4j
@RocketMQMessageListener(consumerGroup = "consumer-group", topic = "add-bonus")
public class AddBonusListener implements RocketMQListener<UserAddBonusMsg> {

    @Autowired
    UserMapper userMapper;

    @Autowired
    BonusEventLogMapper bonusEventLogMapper;

    @Override
    public void onMessage(UserAddBonusMsg msg) {
        User user = userMapper.selectByPrimaryKey(msg.getUserId());
        user.setBonus(user.getBonus() + msg.getBonus());
        userMapper.updateByPrimaryKeySelective(user);

        BonusEventLog bonusEventLog = BonusEventLog.builder().userId(msg.getUserId()).value(msg.getBonus()).event("CONTRIBUTE").createTime(new Date()).description("投稿").build();
        bonusEventLogMapper.insert(bonusEventLog);
    }
}
