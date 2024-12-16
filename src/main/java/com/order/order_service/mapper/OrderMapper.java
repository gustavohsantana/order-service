package com.order.order_service.mapper;

import com.order.order_service.dto.request.OrderRequest;
import com.order.order_service.dto.response.OrderResponse;
import com.order.order_service.dto.response.ProductResponse;
import com.order.order_service.model.Order;
import com.order.order_service.model.Product;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static Order toOrder(OrderRequest orderRequest) {
        List<Product> products = orderRequest.getProducts().stream()
                .map(ProductMapper::toProduct)
                .collect(Collectors.toList());

        Order order = new Order();
        order.setProducts(products);  // Certifique-se de que os produtos estão sendo setados corretamente.
        order.setOrderCode(orderRequest.getOrderCode());

        return order;
    }

    public static OrderResponse toOrderResponse(Order order) {
        List<ProductResponse> productResponses = order.getProducts().stream()
                .map(ProductMapper::toProductResponse)
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .products(productResponses)
                .totalOrder(order.getTotalOrder())
                .orderId(order.getId())
                .orderCode(order.getOrderCode())
                .orderStatus(order.getStatus())
                .build();
    }
}
