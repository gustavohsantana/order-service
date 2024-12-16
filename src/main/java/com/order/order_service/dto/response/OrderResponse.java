package com.order.order_service.dto.response;

import com.order.order_service.model.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class OrderResponse {
    private List<ProductResponse> products;
    private BigDecimal totalOrder;
    private Long orderId;
    private String orderCode;
    private OrderStatus orderStatus;
}
