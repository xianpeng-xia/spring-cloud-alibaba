package com.example.content.configuration;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancer;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xianpeng.xia
 * on 2020/12/6 下午9:35
 */
@Slf4j
public class NacosWeightedRule extends AbstractLoadBalancerRule {

    @Autowired
    NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
        //读取配置文件，并初始化NacosWeightedRule

    }

    @Override
    public Server choose(Object o) {
        try {
            BaseLoadBalancer baseLoadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
            //  想要请求的微服务的名称
            String name = baseLoadBalancer.getName();
            // 服务发现的相关api
            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();
            // nacos client给予权重的负载均衡算法
            Instance instance = namingService.selectOneHealthyInstance(name);
            log.info("LB = {}", baseLoadBalancer);
            log.info("Instance: port = {} , instance = {}", instance.getPort(), instance);
            return new NacosServer(instance);
        } catch (NacosException e) {
            log.error("NacosException ", e);
        }
        return null;
    }
}
