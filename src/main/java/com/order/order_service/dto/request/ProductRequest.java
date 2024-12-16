package com.order.order_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor // Adiciona o construtor sem argumentos
@AllArgsConstructor
public class ProductRequest {
    int productId;
    int quantity;
    BigDecimal price;
    String name;
}