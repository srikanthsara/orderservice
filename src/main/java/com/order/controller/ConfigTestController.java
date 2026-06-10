package com.order.controller;

import com.order.config.OrderProperties;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ConfigTestController {

    private final OrderProperties properties;

    @GetMapping("/config-test")
    public Object test() {
        return properties;
    }
}
