package com.example.blog.content.feign.client;

import com.example.blog.common.domain.dto.user.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author xianpeng.xia
 * on 2020/12/6 下午11:31
 */
@FeignClient(name = "user-center"
    // ,fallback = UserCenterFeignClientFallback.class
   // fallbackFactory = UserCenterFeignClientFallbackFactory.class
)
public interface UserCenterFeignClient {

    // http://user-center/user/{id}
    @GetMapping("/user/{id}")
    UserDTO findById(@PathVariable Integer id);
}
