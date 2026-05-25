package com.order.client;

import com.order.dto.CartResponse;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "cart-service",

        url = "http://localhost:8763/cartservice"
)
public interface CartServiceClient {

    @GetMapping("/cart/{customerId}")
    CartResponse getCart(
            @PathVariable String customerId);

    @DeleteMapping("/cart/clear/{customerId}")
    void clearCart(
            @PathVariable String customerId);
}