package com.example.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author xianpeng.xia
 * on 2020/12/18 上午12:55
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckAuthorization {

    String value();
}
