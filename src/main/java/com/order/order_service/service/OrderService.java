package com.order.order_service.service;

import com.order.order_service.dto.request.OrderRequest;
import com.order.order_service.dto.response.OrderResponse;
import com.order.order_service.mapper.OrderMapper;
import com.order.order_service.model.Order;
import com.order.order_service.model.OrderStatus;
import com.order.order_service.respository.OrderRepository;
import com.order.order_service.utils.OrderCodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public OrderService(OrderRepository orderRepository, RedisTemplate<String, Object> redisTemplate) {
        this.orderRepository = orderRepository;
        this.redisTemplate = redisTemplate;
    }

    @Transactional
    public OrderResponse processOrder(OrderRequest orderRequest) throws NoSuchAlgorithmException {
        String orderCode = OrderCodeGenerator.generateOrderCode(orderRequest);
        Order order = OrderMapper.toOrder(orderRequest);
        order.setTotalOrder(calculateTotalOrder(order));
        order.setOrderCode(orderCode);

        try {
            order.setStatus(OrderStatus.PROCESSED);
            orderRepository.save(order);
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("Pedido já processado com código: {}", orderCode);
            return OrderMapper.toOrderResponse(orderRepository.findByOrderCode(orderCode));
        }

        saveToCache("ORDER_" + order.getId(), order);
        redisTemplate.delete("ALL_ORDERS");

        return OrderMapper.toOrderResponse(order);
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = (List<Order>) redisTemplate.opsForValue().get("ALL_ORDERS");

        if (orders == null) {
            orders = orderRepository.findAll();
            saveToCache("ALL_ORDERS", orders);
        }

        return orders.stream()
                .map(OrderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderById(Long id) {
        Order order = (Order) redisTemplate.opsForValue().get("ORDER_" + id);

        if (order == null) {
            order = orderRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            saveToCache("ORDER_" + id, order);
        }

        return OrderMapper.toOrderResponse(order);
    }

    private void saveToCache(String key, Object value) {
        redisTemplate.opsForValue().set(key, value, 10, TimeUnit.MINUTES);
    }

    BigDecimal calculateTotalOrder(Order order) {
        if (order == null || order.getProducts() == null || order.getProducts().isEmpty()) {
            return BigDecimal.ZERO;
        }

        return order.getProducts().stream()
                .map(product -> product.getPrice().multiply(BigDecimal.valueOf(product.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
