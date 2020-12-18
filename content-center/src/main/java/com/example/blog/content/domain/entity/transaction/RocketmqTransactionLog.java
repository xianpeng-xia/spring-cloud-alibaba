package com.example.blog.content.domain.entity.transaction;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Table(name = "rocketmq_transaction_log")
@Builder
public class RocketmqTransactionLog {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
    @Column(name = "transaction_id")
    private String transactionId;
    private String log;
}