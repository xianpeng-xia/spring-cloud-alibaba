package com.example.common.domain.dto.content;

import com.example.common.enums.AuditStatusEnum;
import lombok.Data;

/**
 * @date 2020/12/10
 * @time 下午7:07
 */
@Data
public class ShareAuditDTO {

    private AuditStatusEnum auditStatus;
    private String reason;
}
