package com.praveen.jpa.event;

import lombok.Builder;

@Builder
public record OrderEvent(
    Long orderId,
    String productName,
    Integer quantity,
    Float amount,
    String orderTime,
    String customerName,
    String status) {}
