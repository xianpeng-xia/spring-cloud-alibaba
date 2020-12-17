package com.example.user.auth;

import com.example.user.jwt.JwtOperator;
import io.jsonwebtoken.Claims;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @date 2020/12/17
 * @time 下午7:56
 */
@Component
@Aspect
public class CheckLoginAspect {

    @Autowired
    JwtOperator jwtOperator;

    @Around("@annotation(com.example.common.auth.CheckLogin)")
    public Object checkLogin(ProceedingJoinPoint point) {
        try {
            // 从header中获取token
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
            HttpServletRequest request = attributes.getRequest();
            String token = request.getHeader("X-Token");
            // 校验token是否合法，不合法抛出异常
            Boolean isValid = jwtOperator.validateToken(token);
            if (!isValid) {
                return new SecurityException("Token is illegal");
            }
            // 如果检验成功，
            Claims claims = jwtOperator.getClaimsFromToken(token);
            request.setAttribute("id", claims.get("id"));
            request.setAttribute("username", claims.get("username"));
            request.setAttribute("role", claims.get("role"));
            return point.proceed();
        } catch (Throwable throwable) {
            return new SecurityException("Token is illegal");
        }
    }
}
