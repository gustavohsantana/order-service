package com.order.order_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "customer_order")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Isso irá gerar o ID automaticamente
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Product> products;

    private BigDecimal totalOrder;

    @Column(nullable = false, unique = true)
    private String orderCode; // Identificador único do pedido

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status; // Status do pedido

    public List<Product> getProducts() {
        return products == null ? new ArrayList<>() : products;  // Retorna uma lista vazia se for null
    }
}
