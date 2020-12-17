package com.example.user.controller;

import com.example.annotation.CheckLogin;
import com.example.common.domain.dto.user.JwtTokenResp;
import com.example.common.domain.dto.user.LoginRespDTO;
import com.example.common.domain.dto.user.UserDTO;
import com.example.common.domain.dto.user.UserLoginDTO;
import com.example.common.domain.dto.user.UserRespDTO;
import com.example.user.domain.entity.user.User;
import com.example.user.jwt.JwtOperator;
import com.example.user.service.UserService;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @date 2020/12/03
 * @time 18:35
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JwtOperator jwtOperator;


    @GetMapping("/{id}")
    @CheckLogin
    public UserDTO findById(@PathVariable Integer id) {
        log.info("我被请求了...");
        return this.userService.findById(id);
    }

    @PostMapping("/login")
    public LoginRespDTO login(@RequestBody UserLoginDTO loginDTO) {
        User user = userService.login(loginDTO);
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        claims.put("role", user.getRole());
        String token = jwtOperator.generateToken(claims);
        Date expirationDate = jwtOperator.getExpirationDateFromToken(token);
        log.info("Token :{} ,username {} , expirationDate {}", token, loginDTO.getUsername(), expirationDate);
        UserRespDTO userResp = UserRespDTO.builder().id(user.getId()).username(user.getUsername()).bonus(user.getBonus()).build();
        JwtTokenResp jwtTokenResp = JwtTokenResp.builder().token(token).expirationTime(expirationDate.getTime()).build();
        LoginRespDTO resp = LoginRespDTO.builder().user(userResp).jwtToken(jwtTokenResp).build();
        return resp;
    }
}
