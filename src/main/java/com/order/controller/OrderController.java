package com.order.controller;

import com.order.dto.CheckoutRequest;
import com.order.entity.OrderMaster;
import com.order.service.OrderService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")

@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping("/checkout")
    public OrderMaster checkout(
            @Valid
            @RequestBody CheckoutRequest request) {

        return service.checkout(request);
    }
}