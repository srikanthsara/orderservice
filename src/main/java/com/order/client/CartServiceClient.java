package com.order.client;

import com.order.dto.CartResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cartservice")
public interface CartServiceClient {

    @GetMapping("/cart/{customerId}")
    CartResponse getCart(@PathVariable String customerId);

    @DeleteMapping("/cart/clear/{customerId}")
    void clearCart(@PathVariable String customerId);
}
