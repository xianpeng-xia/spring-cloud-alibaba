package com.example.content.service;

import com.example.common.domain.dto.content.ShareDTO;
import com.example.common.domain.dto.user.UserDTO;
import com.example.content.dao.share.ShareMapper;
import com.example.content.domain.entity.share.Share;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
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
    DiscoveryClient discoveryClient;

    @Autowired
    RestTemplate restTemplate;

    public ShareDTO findById(Integer id) {
        // 获取分享详情
        Share share = this.shareMapper.selectByPrimaryKey(id);
        // 发布人id
        Integer userId = share.getUserId();
        // String targetUrl = getUserCenterTargetUrl(userId);
        //  通过ribbon配置负载均衡获得地址
        String targetURL = "http://user-center/user/" + userId;

        UserDTO user = restTemplate.getForObject(targetURL, UserDTO.class);
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

    /**
     * @param userId userId
     * @return 服务的真实url
     *
     * 使用discoveryClient获取服务地址
     */
    private String getUserCenterTargetUrl(Integer userId) {
        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");
        String targetURL = instances.stream().map(instance -> instance.getUri() + "/user/" + userId)
            .findFirst().orElseThrow(() -> new IllegalArgumentException("No instance"));
        log.info("Target url : {}", targetURL);
        return targetURL;
    }
}
