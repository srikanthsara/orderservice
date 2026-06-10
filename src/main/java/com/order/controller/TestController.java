package com.order.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Value("${order.tax.gst-percentage:999}")
    private String gst;

    @GetMapping("/gst")
    public String gst() {
        return gst;
    }
}
