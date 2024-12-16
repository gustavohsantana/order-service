package com.order.order_service.mapper;

import com.order.order_service.dto.request.ProductRequest;
import com.order.order_service.dto.response.ProductResponse;
import com.order.order_service.model.Product;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

public class ProductMapperTest {

    @Test
    void testToProduct() {
        // Usando o builder para criar o ProductRequest
        ProductRequest request = ProductRequest.builder()
                .productId(1)
                .quantity(100)
                .price(BigDecimal.valueOf(10.0))
                .name("Product 1")
                .build();

        Product product = ProductMapper.toProduct(request);

        assertNotNull(product);
        assertEquals(request.getProductId(), product.getProductId());
        assertEquals(request.getName(), product.getName());
        assertEquals(request.getQuantity(), product.getQuantity());
        assertEquals(request.getPrice(), product.getPrice());
    }

    @Test
    void testToProductResponse() {
        // Usando o builder para criar o Product
        Product product = Product.builder()
                .id(1L)
                .productId(1)
                .name("Product 1")
                .quantity(100)
                .price(BigDecimal.valueOf(10.0))
                .totalSum(BigDecimal.valueOf(1000))
                .build();

        ProductResponse productResponse = ProductMapper.toProductResponse(product);

        assertNotNull(productResponse);
        assertEquals(product.getProductId(), productResponse.getProductId());
        assertEquals(product.getName(), productResponse.getName());
        assertEquals(product.getQuantity(), productResponse.getQuantity());
        assertEquals(product.getTotalSum(), productResponse.getTotal());
    }
}
