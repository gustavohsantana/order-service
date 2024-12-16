package com.order.order_service.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderRequest {
    List<ProductRequest> products;
    private String orderCode;
    private Long customerId;
}