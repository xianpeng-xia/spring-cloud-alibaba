package com.example.content.configuration;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

/**
 * @author xianpeng.xia
 * on 2020/12/6 下午10:51
 */
@Slf4j
public class NacosFinalRule extends AbstractLoadBalancerRule {

    @Autowired
    NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object o) {
        try {
            // 优先选择相同集群下符合metadata的实例
            // 如果没有则选择所有集群下符合metadata的实例
            String clusterName = nacosDiscoveryProperties.getClusterName();
            String targetVersion = nacosDiscoveryProperties.getMetadata().get("targetVersion");

            BaseLoadBalancer baseLoadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
            //  想要请求的微服务的名称
            String name = baseLoadBalancer.getName();
            // 服务发现的相关api
            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();
            // 1 找到指定服务的所有实力 A
            List<Instance> instances = namingService.selectInstances(name, true);
            // 2 筛选出元数据匹配的实例 B
            List<Instance> metadataMatchinstances = instances;
            if (StringUtils.isNotBlank(targetVersion)) {
                metadataMatchinstances = instances.stream()
                    .filter(instance -> StringUtils.equals(instance.getMetadata().get("version"), targetVersion))
                    .collect(Collectors.toList());

                if (CollectionUtils.isEmpty(metadataMatchinstances)) {
                    log.error("No matched instance,version: {}", targetVersion);
                }
            }
            // 3 过滤相同集群下的所有实例 C
            List<Instance> sameClusterInstances = metadataMatchinstances.stream()
                .filter(instance -> Objects.equals(instance.getClusterName(), clusterName)
                ).collect(Collectors.toList());
            // 4 如果B是空就用B
            List<Instance> instancesToBeChosen = CollectionUtils.isEmpty(sameClusterInstances) ? metadataMatchinstances : sameClusterInstances;
            // 4 基于权重的负载均衡算法 返回一个实例
            Instance instance = ExtendBalance._getHostByRandomWeight(instancesToBeChosen);
            log.info("instance = {}", instance);
            return new NacosServer(instance);
        } catch (NacosException e) {
            e.printStackTrace();
        }

        return null;
    }
}
