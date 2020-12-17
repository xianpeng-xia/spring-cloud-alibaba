package com.example.common.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @date 2020/12/17
 * @time 下午5:14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenResp {

    private Long expirationTime;
    private String token;
}
