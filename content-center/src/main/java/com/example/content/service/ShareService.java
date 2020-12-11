package com.example.content.service;

import com.example.common.domain.dto.content.ShareAuditDTO;
import com.example.common.domain.dto.content.ShareDTO;
import com.example.common.domain.dto.message.UserAddBonusMsg;
import com.example.common.domain.dto.user.UserDTO;
import com.example.content.dao.share.ShareMapper;
import com.example.content.domain.entity.share.Share;
import com.example.content.feign.client.UserCenterFeignClient;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.AsyncRestTemplate;
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
    DiscoveryClient discoveryClient;

    @Autowired
    UserCenterFeignClient userCenterFeignClient;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RocketMQTemplate rocketMQTemplate;

    public ShareDTO findById(Integer id) {
        // 获取分享详情
        Share share = this.shareMapper.selectByPrimaryKey(id);
        // 发布人id
        Integer userId = share.getUserId();
        // 第一种方式：通过DiscoveryClient调用
        // UserDTO user = getUserByDiscoveryClient(userId);
        // 第二种方式：通过ribbon配置负载均衡获得
        // UserDTO user = getUserByRibbon(userId);
        // 第三种方式：feign
        UserDTO user = userCenterFeignClient.findById(userId);
        ShareDTO shareDTO = new ShareDTO();
        // 消息的装配
        BeanUtils.copyProperties(share, shareDTO);
        shareDTO.setWxNickname(user.getUsername());
        return shareDTO;
    }


    public Share auditShare(Integer id, ShareAuditDTO dto) {
        Share share = shareMapper.selectByPrimaryKey(id);
        if (share == null) {
            throw new IllegalArgumentException("No such share");
        }
        if (ObjectUtils.notEqual("NOT_YET", share.getAuditStatus())) {
            throw new IllegalArgumentException("Has been passed or rejected");
        }
        share.setAuditStatus(dto.getAuditStatus().name());
        share.setReason(dto.getReason());
        shareMapper.updateByPrimaryKey(share);

        // 加积分异步执行
        UserAddBonusMsg userAddBonusMsg = UserAddBonusMsg.builder().userId(share.getUserId()).bonus(50).build();
        rocketMQTemplate.convertAndSend("add-bonus", userAddBonusMsg);

        return share;
    }

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        UserDTO user = restTemplate.getForObject("http://localhost:8080/user/1", UserDTO.class);
        log.info("{}", user);
    }

    /**
     * @param userId userId
     * @return 服务的真实url
     * 使用discoveryClient调用远程服务
     */
    private UserDTO getUserByDiscoveryClient(Integer userId) {
        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");
        String targetURL = instances.stream().map(instance -> instance.getUri() + "/user/" + userId)
            .findFirst().orElseThrow(() -> new IllegalArgumentException("No instance"));
        log.info("Target url : {}", targetURL);
        UserDTO user = restTemplate.getForObject(targetURL, UserDTO.class);
        return user;
    }

    /**
     * @param userId userId
     * @return 使用Ribbon负载均衡调用远程服务
     */
    private UserDTO getUserByRibbon(Integer userId) {
        String targetURL = "http://user-center/user/" + userId;
        UserDTO user = restTemplate.getForObject(targetURL, UserDTO.class);
        return user;
    }
}
