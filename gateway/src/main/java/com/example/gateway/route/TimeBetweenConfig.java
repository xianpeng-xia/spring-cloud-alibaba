package com.example.gateway.route;

import java.time.LocalTime;
import lombok.Data;

/**
 * @date 2020/12/15
 * @time 下午5:32
 */
@Data
public class TimeBetweenConfig {

    private LocalTime begin;
    private LocalTime end;
}
