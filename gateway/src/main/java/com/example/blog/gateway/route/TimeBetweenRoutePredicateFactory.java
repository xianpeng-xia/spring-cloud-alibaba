package com.example.blog.gateway.route;


import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * @date 2020/12/15
 * @time 下午5:29
 */
@Component
public class TimeBetweenRoutePredicateFactory extends AbstractRoutePredicateFactory<TimeBetweenConfig> {

    public TimeBetweenRoutePredicateFactory() {
        super(TimeBetweenConfig.class);
    }

    @Override
    public Predicate<ServerWebExchange> apply(TimeBetweenConfig config) {
        LocalTime begin = config.getBegin();
        LocalTime end = config.getEnd();

        return exchange -> {
            LocalTime now = LocalTime.now();
            return now.isAfter(begin) && now.isBefore(end);
        };
    }

    /**
     * @return 配置文件中参数顺序以及对应config字段
     */
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("begin", "end");
    }
}
