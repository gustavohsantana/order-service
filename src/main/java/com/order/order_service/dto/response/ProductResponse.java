package com.order.order_service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductResponse {
    private int productId;
    private String name;
    private BigDecimal price;
    private int quantity;
    private BigDecimal total;
}
