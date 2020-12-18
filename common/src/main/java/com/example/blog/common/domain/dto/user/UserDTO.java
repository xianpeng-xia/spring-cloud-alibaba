package com.example.blog.common.domain.dto.user;

import java.util.Date;
import lombok.Data;

/**
 * @date 2020/12/04
 * @time 下午12:44
 */
@Data
public class UserDTO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
