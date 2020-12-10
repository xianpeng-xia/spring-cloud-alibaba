package com.example.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @date 2020/12/10
 * @time 下午7:10
 */
@Getter
@AllArgsConstructor
public enum AuditStatusEnum {
    NOT_YET,
    PASS,
    REJECT;
}
