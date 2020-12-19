package com.example.blog.content.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @date 2020/12/10
 * @time 下午5:17
 * 区分来源
 */
@Component
public class MyRequestOriginParser implements RequestOriginParser {

    @Override
    public String parseOrigin(HttpServletRequest request) {
        // 从请求参数中获取名为origin的参数 如果获取不到就抛出异常 建议放header中
        String origin = request.getParameter("origin");
        if (StringUtils.isEmpty(origin)) {
           // throw new IllegalArgumentException("origin must be specified");
        }
        return origin;
    }
}
