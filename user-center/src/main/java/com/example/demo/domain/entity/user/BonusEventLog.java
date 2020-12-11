package com.example.demo.domain.entity.user;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Table(name = "bonus_event_log")
public class BonusEventLog {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    private Integer value;

    private String event;

    @Column(name = "create_time")
    private Integer createTime;

    private String description;
}