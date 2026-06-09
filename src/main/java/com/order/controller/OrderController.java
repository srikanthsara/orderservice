package com.order.controller;

import com.order.dto.CheckoutRequest;
import com.order.entity.OrderMaster;
import com.order.service.OrderService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RefreshScope
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping("/checkout")
    public OrderMaster checkout(
            @Valid
            @RequestBody CheckoutRequest request) {

        return service.checkout(request);
    }

    @GetMapping("/customer/{customerId}")
    public Page<OrderMaster> getOrders(
            @PathVariable String customerId,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "5")  int size) {

        return service.getOrders(customerId,page,size);
    }
}