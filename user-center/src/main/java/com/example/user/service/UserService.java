package com.example.user.service;

import com.example.common.domain.dto.message.UserAddBonusMsg;
import com.example.common.domain.dto.user.UserDTO;
import com.example.common.domain.dto.user.UserLoginDTO;
import com.example.user.dao.user.BonusEventLogMapper;
import com.example.user.dao.user.UserMapper;
import com.example.user.domain.entity.user.BonusEventLog;
import com.example.user.domain.entity.user.User;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @date 2020/12/03
 * @time 18:39
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    BonusEventLogMapper bonusEventLogMapper;

    public UserDTO findById(int id) {
        User user = userMapper.selectByPrimaryKey(id);
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }

    public User login(UserLoginDTO loginDTO) {
        User user = userMapper.selectOne(User.builder().email(loginDTO.getEmail()).build());
        if (user == null) {
            User newUser = User.builder().email(loginDTO.getEmail()).username(loginDTO.getUsername()).bonus(100).role("user").createTime(new Date()).updateTime(new Date()).build();
            userMapper.insertSelective(newUser);
            return newUser;
        }
        return user;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addBonus(UserAddBonusMsg msg) {

        User user = userMapper.selectByPrimaryKey(msg.getUserId());
        user.setBonus(user.getBonus() + msg.getBonus());
        userMapper.updateByPrimaryKeySelective(user);

        BonusEventLog bonusEventLog = BonusEventLog.builder().userId(msg.getUserId()).value(msg.getBonus()).event("CONTRIBUTE").createTime(new Date()).description("投稿").build();
        bonusEventLogMapper.insert(bonusEventLog);

        log.info("Add bonus message {}", msg);
    }
}
