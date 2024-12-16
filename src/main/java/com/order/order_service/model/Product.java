package com.order.order_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private int productId;
    private int quantity;
    private BigDecimal price;
    private BigDecimal totalSum;
    private String name;

    @ManyToOne
    @JoinColumn(name = "order_id")  // Coluna que vai armazenar a chave estrangeira
    private Order order;
}
