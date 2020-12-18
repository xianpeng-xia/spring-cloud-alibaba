package com.example.blog.content.feign.client.fallbackfactory;

import com.example.blog.common.domain.dto.user.UserDTO;
import com.example.blog.content.feign.client.UserCenterFeignClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author xianpeng.xia
 * on 2020/12/9 下午11:00
 */
@Component
@Slf4j
public class UserCenterFeignClientFallbackFactory implements FallbackFactory<UserCenterFeignClient> {

    @Override
    public UserCenterFeignClient create(Throwable throwable) {

        return new UserCenterFeignClient() {

            @Override
            public UserDTO findById(Integer id) {
                log.warn("Exception: ", throwable);
                UserDTO userDTO = new UserDTO();
                userDTO.setUsername("default user");
                return userDTO;
            }
        };
    }
}
