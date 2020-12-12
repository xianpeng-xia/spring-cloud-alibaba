package com.example.content.rocketmq;

import com.example.common.domain.dto.content.ShareAuditDTO;
import com.example.content.dao.share.ShareMapper;
import com.example.content.dao.transaction.RocketmqTransactionLogMapper;
import com.example.content.domain.entity.transaction.RocketmqTransactionLog;
import com.example.content.domain.entity.transaction.RocketmqTransactionLog.RocketmqTransactionLogBuilder;
import com.example.content.service.ShareService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

/**
 * @date 2020/12/12
 * @time 下午3:34
 */
@RocketMQTransactionListener(txProducerGroup = "tx-add-bonus-group")
@Slf4j
public class AddBonusTransactionListener implements RocketMQLocalTransactionListener {

    @Autowired
    ShareService shareService;

    @Autowired
    RocketmqTransactionLogMapper rocketmqTransactionLogMapper;

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object args) {
        MessageHeaders headers = message.getHeaders();
        String transactionId = (String) headers.get(RocketMQHeaders.TRANSACTION_ID);
        Integer shareId = (Integer) headers.get("share_id");
        try {
            shareService.auditByIdWithRocketMqLog(shareId, (ShareAuditDTO) args, transactionId);
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
