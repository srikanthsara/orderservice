package com.order.client;

import com.order.dto.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "grocerysearchservice")
public interface GrocerySearchClient {
    @GetMapping("/products/{productId}")
    Product getProductById(@PathVariable("productId")  String productId);
}
