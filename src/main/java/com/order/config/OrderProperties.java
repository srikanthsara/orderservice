package com.order.config;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.math.BigDecimal;


@ConfigurationProperties(prefix = "order")
@EnableConfigurationProperties
@Data
public class OrderProperties {

    @NotNull
    private Shipping shipping = new Shipping();

    @NotNull
    private Discount discount = new Discount();

    @NotNull
    private Status status = new Status();

    @Data
    public static class Shipping {
        private BigDecimal charge = BigDecimal.ZERO;
    }

    @Data
    public static class Discount {
        private BigDecimal defaultDiscount = BigDecimal.ZERO;
    }

    @Data
    public static class Status {

        private String paymentPending;
        private String inventoryPending;
        private String orderCreated;
    }
}
