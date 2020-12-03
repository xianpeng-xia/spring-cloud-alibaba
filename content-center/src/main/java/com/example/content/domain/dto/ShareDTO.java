package com.example.content.domain.dto;

import java.util.Date;
import lombok.Data;

/**
 * @date 2020/12/03
 * @time 20:25
 */
@Data
public class ShareDTO {

    private Integer userId;
    private Integer title;
    private Date createTime;
    private Integer updateTime;
    private Byte isOriginal;
    private String author;
    private String cover;
    private String summary;
    private Integer price;
    private String downloadUrl;
    private Integer buyCount;
    private Byte showFlag;
    private String auditStatus;
    private String reason;
    private String wxNickname;
}
