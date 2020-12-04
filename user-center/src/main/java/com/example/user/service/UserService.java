package com.example.user.service;

import com.example.common.domain.dto.user.UserDTO;
import com.example.user.dao.user.UserMapper;
import com.example.user.domain.entity.user.User;
import org.springframework.beans.BeanUtils;
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

    public UserDTO findById(int id) {
        User user = userMapper.selectByPrimaryKey(id);
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }

}
