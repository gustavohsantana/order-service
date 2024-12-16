package com.order.order_service.mapper;

import com.order.order_service.dto.request.OrderRequest;
import com.order.order_service.dto.request.ProductRequest;
import com.order.order_service.dto.response.OrderResponse;
import com.order.order_service.model.Order;
import com.order.order_service.model.OrderStatus;
import com.order.order_service.model.Product;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderMapperTest {

    @Test
    void testToOrder() {
        // Criando ProductRequest com o builder
        ProductRequest productRequest = ProductRequest.builder()
                .productId(1)
                .quantity(100)
                .price(BigDecimal.valueOf(10.0))
                .name("Product 1")
                .build();

        // Criando OrderRequest com o builder
        OrderRequest orderRequest = OrderRequest.builder()
                .orderCode("ORDER123")
                .products(List.of(productRequest))
                .build();

        // Convertendo OrderRequest para Order
        Order order = OrderMapper.toOrder(orderRequest);

        assertNotNull(order);
        assertEquals(orderRequest.getOrderCode(), order.getOrderCode());
        assertEquals(1, order.getProducts().size());
        assertEquals(orderRequest.getProducts().get(0).getProductId(), order.getProducts().get(0).getProductId());
    }

    @Test
    void testToOrderResponse() {
        // Criando Product com o builder
        Product product = Product.builder()
                .id(1L)
                .productId(1)
                .name("Product 1")
                .quantity(100)
                .price(BigDecimal.valueOf(10.0))
                .totalSum(BigDecimal.valueOf(1000))
                .build();

        // Criando Order com o builder
        Order order = Order.builder()
                .id(1L)
                .orderCode("ORDER123")
                .status(OrderStatus.PENDING)
                .totalOrder(BigDecimal.valueOf(1000))
                .products(List.of(product))
                .build();

        // Convertendo Order para OrderResponse
        OrderResponse orderResponse = OrderMapper.toOrderResponse(order);

        assertNotNull(orderResponse);
        assertEquals(order.getOrderCode(), orderResponse.getOrderCode());
        assertEquals(order.getTotalOrder(), orderResponse.getTotalOrder());
        assertEquals(order.getStatus(), orderResponse.getOrderStatus());
        assertEquals(1, orderResponse.getProducts().size());
        assertEquals(order.getProducts().get(0).getProductId(), orderResponse.getProducts().get(0).getProductId());
    }
}
