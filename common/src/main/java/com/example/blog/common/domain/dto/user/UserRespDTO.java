package com.example.blog.common.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @date 2020/12/17
 * @time 下午5:16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRespDTO {

    private Integer id;
    private String username;
    private Integer bonus;
}
