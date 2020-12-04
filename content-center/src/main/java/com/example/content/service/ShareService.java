package com.example.content.service;

import com.example.common.domain.dto.content.ShareDTO;
import com.example.common.domain.dto.user.UserDTO;
import com.example.content.dao.share.ShareMapper;
import com.example.content.domain.entity.share.Share;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @date 2020/12/03
 * @time 20:24
 */
@Service
@Slf4j
public class ShareService {

    @Autowired
    private ShareMapper shareMapper;

    @Autowired
    RestTemplate restTemplate;

    public ShareDTO findById(Integer id) {
        // 获取分享详情
        Share share = this.shareMapper.selectByPrimaryKey(id);
        // 发布人id
        Integer userId = share.getUserId();
        UserDTO user = restTemplate.getForObject("http://localhost:8080/user/" + userId, UserDTO.class);

        ShareDTO shareDTO = new ShareDTO();
        // 消息的装配
        BeanUtils.copyProperties(share, shareDTO);
        shareDTO.setWxNickname(user.getUsername());
        return shareDTO;
    }

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        UserDTO user = restTemplate.getForObject("http://localhost:8080/user/1", UserDTO.class);
        log.info("{}", user);
    }
}
