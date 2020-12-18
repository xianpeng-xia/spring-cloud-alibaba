package com.example.blog.auth;

import com.example.blog.annotation.CheckAuthorization;
import com.example.blog.auth.jwt.JwtOperator;
import io.jsonwebtoken.Claims;
import java.lang.reflect.Method;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
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
public class CheckAuthAspect {

    @Autowired
    JwtOperator jwtOperator;

    @Around("@annotation(com.example.blog.annotation.CheckLogin)")
    public Object checkLogin(ProceedingJoinPoint point) throws Throwable {
        checkToken();
        return point.proceed();
    }

    @Around("@annotation(com.example.blog.annotation.CheckAuthorization)")
    public Object checkAuthorization(ProceedingJoinPoint point) throws Throwable {
        // 验证token是否合法
        try {

            checkToken();
            // 验证角色是否匹配
            HttpServletRequest request = getHttpServletRequest();
            String role = (String) request.getAttribute("role");
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            CheckAuthorization annotation = method.getAnnotation(CheckAuthorization.class);
            String value = annotation.value();
            if (!Objects.equals(role, value)) {
                throw new SecurityException("Permission denied !!!");
            }
        } catch (Throwable throwable) {
            throw new SecurityException("Permission denied !!!", throwable);
        }
        return point.proceed();
    }

    private HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = attributes.getRequest();
        return request;
    }

    private void checkToken() {
        try {
            // 从header中获取token
            HttpServletRequest request = getHttpServletRequest();
            String token = request.getHeader("X-Token");
            // 校验token是否合法，不合法抛出异常
            Boolean isValid = jwtOperator.validateToken(token);
            if (!isValid) {
                throw new SecurityException("Token is illegal");
            }
            // 如果检验成功，
            Claims claims = jwtOperator.getClaimsFromToken(token);
            request.setAttribute("id", claims.get("id"));
            request.setAttribute("username", claims.get("username"));
            request.setAttribute("role", claims.get("role"));

        } catch (SecurityException e) {
            throw new SecurityException("Token is illegal");
        }
    }
}
