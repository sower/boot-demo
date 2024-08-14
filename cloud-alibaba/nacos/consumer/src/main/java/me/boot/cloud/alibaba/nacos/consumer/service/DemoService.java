package me.boot.cloud.alibaba.nacos.consumer.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * DemoService
 *
 * @since 2024/06/19
 **/
@Service
@RequiredArgsConstructor
public class DemoService {

    private final RestTemplate restTemplate;

    private final DiscoveryClient discoveryClient;

    public void echo(String str) {
        // 获取服务 `demo-provider` 对应的实例列表
        List<ServiceInstance> instances = discoveryClient.getInstances("demo-provider");
        // instance = loadBalancerClient.choose("demo-provider");

        // 发起调用
        if (instances.isEmpty()) {
            throw new IllegalStateException("获取不到实例");
        }
        ServiceInstance instance = instances.get(0);
        String targetUrl = instance.getUri() + "/echo?name=" + str;
        String response = restTemplate.getForObject(targetUrl, String.class);
        // 返回结果
        System.err.println(response);
    }

}
