package com.example.content.feign.client.fallback;

import com.example.common.domain.dto.user.UserDTO;
import com.example.content.feign.client.UserCenterFeignClient;
import org.springframework.stereotype.Component;

/**
 * @author xianpeng.xia
 * on 2020/12/9 下午10:56
 */
@Component
public class UserCenterFeignClientFallback implements UserCenterFeignClient {

    @Override
    public UserDTO findById(Integer id) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("default user");
        return userDTO;
    }
}
