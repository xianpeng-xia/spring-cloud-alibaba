package com.example.blog.content.service;

import com.alibaba.fastjson.JSON;
import com.example.blog.common.domain.dto.content.ShareAuditDTO;
import com.example.blog.common.domain.dto.content.ShareDTO;
import com.example.blog.common.domain.dto.message.UserAddBonusMsg;
import com.example.blog.common.domain.dto.user.UserDTO;
import com.example.blog.common.enums.AuditStatusEnum;
import com.example.blog.content.dao.share.ShareMapper;
import com.example.blog.content.dao.transaction.RocketmqTransactionLogMapper;
import com.example.blog.content.domain.entity.share.Share;
import com.example.blog.content.domain.entity.transaction.RocketmqTransactionLog;
import com.example.blog.content.feign.client.UserCenterFeignClient;
import com.example.blog.content.rocketmq.AddBonusSource;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    RocketmqTransactionLogMapper rocketmqTransactionLogMapper;

    @Autowired
    AddBonusSource addBonusSource;

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
        // 如果通过加积分,异步执行
        if (dto.getAuditStatus().equals(AuditStatusEnum.PASS)) {
            String transactionId = UUID.randomUUID().toString();
            UserAddBonusMsg userAddBonusMsg = UserAddBonusMsg.builder().userId(share.getUserId()).bonus(50).build();
            // 使用stream发送消息
            streamSend(transactionId, id, userAddBonusMsg, dto);
        } else {
            auditByIdInDB(id, dto);
        }

        return share;
    }

    private void streamSend(String transactionId, Integer id, UserAddBonusMsg userAddBonusMsg, ShareAuditDTO dto) {
        Message<UserAddBonusMsg> message = MessageBuilder
            .withPayload(userAddBonusMsg)
            .setHeader(RocketMQHeaders.TRANSACTION_ID, transactionId)
            .setHeader("share_id", id)
            .setHeader("dto", JSON.toJSONString(dto))
            .build();
        boolean result = addBonusSource.output().send(message);
        log.info("Send {}", result);
    }

    private void rocketMQTemplateSend(String transactionId, Integer id, UserAddBonusMsg userAddBonusMsg, ShareAuditDTO dto) {
        Message<UserAddBonusMsg> message = MessageBuilder
            .withPayload(userAddBonusMsg)
            .setHeader(RocketMQHeaders.TRANSACTION_ID, transactionId)
            .setHeader("share_id", id)
            .build();
        rocketMQTemplate.sendMessageInTransaction(
            "tx-add-bonus-group",
            "add-bonus",
            message,
            dto);
    }

    // 本地事务
    @Transactional(rollbackFor = Exception.class)
    public void auditByIdInDB(Integer id, ShareAuditDTO dto) {
        Share share = Share.builder().id(id).auditStatus(dto.getAuditStatus().name()).reason(dto.getReason()).build();
        shareMapper.updateByPrimaryKeySelective(share);
    }

    @Transactional(rollbackFor = Exception.class)
    public void auditByIdWithRocketMqLog(Integer id, ShareAuditDTO auditDTO, String transactionId) {
        auditByIdInDB(id, auditDTO);
        RocketmqTransactionLog rocketmqTransactionLog = RocketmqTransactionLog.builder().transactionId(transactionId).log("audit").build();
        rocketmqTransactionLogMapper.insertSelective(rocketmqTransactionLog);
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
