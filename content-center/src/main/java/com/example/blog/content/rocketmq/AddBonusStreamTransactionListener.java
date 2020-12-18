package com.example.blog.content.rocketmq;

import com.alibaba.fastjson.JSON;
import com.example.blog.common.domain.dto.content.ShareAuditDTO;
import com.example.blog.content.dao.transaction.RocketmqTransactionLogMapper;
import com.example.blog.content.domain.entity.transaction.RocketmqTransactionLog;
import com.example.blog.content.service.ShareService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

/**
 * @date 2020/12/12
 * @time 下午3:34
 */
@RocketMQTransactionListener(txProducerGroup = "tx-stream-add-bonus-group")
@Slf4j
public class AddBonusStreamTransactionListener implements RocketMQLocalTransactionListener {

    @Autowired
    ShareService shareService;

    @Autowired
    RocketmqTransactionLogMapper rocketmqTransactionLogMapper;

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object args) {
        MessageHeaders headers = message.getHeaders();
        String transactionId = (String) headers.get(RocketMQHeaders.TRANSACTION_ID);
        Integer shareId = Integer.valueOf((String) headers.get("share_id"));
        ShareAuditDTO dto = JSON.parseObject((String) headers.get("dto"), ShareAuditDTO.class);
        try {
            shareService.auditByIdWithRocketMqLog(shareId, dto, transactionId);
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        MessageHeaders headers = message.getHeaders();
        String transactionId = (String) headers.get(RocketMQHeaders.TRANSACTION_ID);

        RocketmqTransactionLog transactionLog = rocketmqTransactionLogMapper.selectOne(RocketmqTransactionLog.builder().transactionId(transactionId).build());

        return transactionLog != null ? RocketMQLocalTransactionState.COMMIT : RocketMQLocalTransactionState.ROLLBACK;
    }
}
