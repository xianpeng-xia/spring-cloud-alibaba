package com.example.demo.service;

import com.example.demo.dao.user.UserMapper;
import com.example.demo.domain.entity.user.User;
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
