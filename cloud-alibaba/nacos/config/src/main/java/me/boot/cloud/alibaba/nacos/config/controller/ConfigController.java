package me.boot.cloud.alibaba.nacos.config.controller;

import lombok.RequiredArgsConstructor;
import me.boot.cloud.alibaba.nacos.config.bean.App;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ConfigController
 *
 * @since 2024/06/23
 **/
@RequestMapping(value = "app")
@RestController
@RefreshScope
@RequiredArgsConstructor
public class ConfigController {

    private final App app;

    @Value("${app.tag}")
    private String appTag;

    @GetMapping
    public App getApp() {
        return app;
    }

    @GetMapping("/tag")
    public String getAppTag() {
        return appTag;
    }

}
