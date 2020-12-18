package com.example.blog.content.sentinel;


import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.UrlCleaner;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

/**
 * @date 2020/12/10
 * @time 下午5:36
 */
@Component
@Slf4j
public class MyUrlCleaner implements UrlCleaner {

    @Override
    public String clean(String origin) {
        // log.info("Origin url {}", origin);
        // 如share/1与 share/2应在同一规则下
        String[] split = origin.split("/");
        return Arrays.asList(split).stream()
            .map(s -> {
                if (NumberUtils.isNumber(s)) {
                    return "{numbser}";
                }
                return s;
            })
            .reduce((a, b) -> a + b)
            .orElse("");
    }
}
