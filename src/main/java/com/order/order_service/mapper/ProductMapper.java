package com.order.order_service.mapper;

import com.order.order_service.dto.request.ProductRequest;
import com.order.order_service.dto.response.ProductResponse;
import com.order.order_service.model.Product;

public class ProductMapper {

    public static Product toProduct(ProductRequest request) {
        return Product.builder()
                .productId(request.getProductId())
                .name(request.getName())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .build();
    }

    public static ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .total(product.getTotalSum())
                .build();
    }

}
