package com.example.user.service;

import com.example.user.dao.user.UserMapper;
import com.example.user.domain.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2020/12/03
 * @time 18:39
 */
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public User findById(int id) {
        return userMapper.selectByPrimaryKey(id);
    }

}
