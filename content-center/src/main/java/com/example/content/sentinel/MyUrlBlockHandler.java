package com.example.content.sentinel;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlBlockHandler;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @date 2020/12/10
 * @time 下午3:47
 * 错误页
 */
@Component
public class MyUrlBlockHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {
        ErrorMsg errorMsg = null;
        if (e instanceof AuthorityException) {
            errorMsg = ErrorMsg.builder().status(100).msg("AuthorityException").build();
        } else if (e instanceof DegradeException) {
            errorMsg = ErrorMsg.builder().status(101).msg("DegradeException").build();
        } else if (e instanceof FlowException) {
            errorMsg = ErrorMsg.builder().status(102).msg("FlowException").build();
        } else if (e instanceof ParamFlowException) {
            errorMsg = ErrorMsg.builder().status(103).msg("ParamFlowException").build();
        } else if (e instanceof SystemBlockException) {
            errorMsg = ErrorMsg.builder().status(104).msg("SystemBlockException").build();
        }

        httpServletResponse.setStatus(500);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpServletResponse.setContentType("application/json;charset=UTF-8");

        new ObjectMapper().writeValue(httpServletResponse.getWriter(), errorMsg);
    }
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class ErrorMsg {

    private Integer status;
    private String msg;
}
