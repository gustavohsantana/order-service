package com.order.order_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.order.order_service.model.Order;
import com.order.order_service.model.Product;
import com.order.order_service.respository.OrderRepository;
import com.order.order_service.dto.response.OrderResponse;
import com.order.order_service.utils.OrderCodeGenerator;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testGetOrderFromCache() {
        // Mock do ValueOperations
        ValueOperations<String, Object> valueOperations = mock(ValueOperations.class);

        // Configurando o RedisTemplate para retornar o mock de ValueOperations
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // Configurando o comportamento do mock de ValueOperations
        Order cachedOrder = new Order();
        cachedOrder.setId(1L);
        cachedOrder.setTotalOrder(BigDecimal.valueOf(100.0));

        when(valueOperations.get("order:1")).thenReturn(cachedOrder);

        // Chamar o método que utiliza o RedisTemplate
        Order order = (Order) redisTemplate.opsForValue().get("order:1");

        // Verificar o resultado
        assertEquals(1L, order.getId());
        assertEquals(100.0, order.getTotalOrder().doubleValue());
    }

    // Teste 1: Soma do total do pedido
    @Test
    void testCalculateTotalOrder() {
        // Setup: Criar uma lista de produtos
        List<Product> products = new ArrayList<>();
        products.add(new Product(null, 1, 2, BigDecimal.valueOf(10.0), null, "Product A", null));
        products.add(new Product(null, 2, 1, BigDecimal.valueOf(20.0), null, "Product B", null));

        Order order = new Order();
        order.setProducts(products);

        // Chamar o método privado via Service
        BigDecimal total = orderService.calculateTotalOrder(order);

        // Verificar resultado
        assertEquals(BigDecimal.valueOf(40.0), total);
    }

    // Teste 2: Recuperar todos os pedidos
    @Test
    void getAllOrders_success() {
        // Criação de pedidos
        List<Order> orders = new ArrayList<>();
        Order order1 = new Order();
        order1.setId(1L);
        order1.setTotalOrder(BigDecimal.valueOf(100.0));

        Order order2 = new Order();
        order2.setId(2L);
        order2.setTotalOrder(BigDecimal.valueOf(200.0));

        orders.add(order1);
        orders.add(order2);

        // Mockando o comportamento do repositório
        when(orderRepository.findAll()).thenReturn(orders);

        // Mock do RedisTemplate para retornar um ValueOperations
        ValueOperations<String, Object> valueOperations = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // Chama o método de serviço
        List<OrderResponse> response = orderService.getAllOrders();

        // Verificação
        assertEquals(2, response.size());
        assertEquals(100.0, response.get(0).getTotalOrder().doubleValue());
        assertEquals(200.0, response.get(1).getTotalOrder().doubleValue());
    }

    // Teste 3: Recuperar pedido por ID
    @Test
    void testGetOrderById() {
        // Mock do ValueOperations para Redis
        ValueOperations<String, Object> valueOperations = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // Certificar que nenhuma entrada em cache será usada no teste
        when(valueOperations.get(any())).thenReturn(null);

        // Setup: Simular um pedido retornado pelo repositório
        Order mockOrder = new Order();
        mockOrder.setId(1L);
        mockOrder.setTotalOrder(BigDecimal.valueOf(150.0));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(mockOrder));

        // Chamar o método
        OrderResponse response = orderService.getOrderById(1L);

        // Verificar se o pedido foi mapeado corretamente
        assertEquals(1L, response.getOrderId());
        assertEquals(150.0, response.getTotalOrder().doubleValue());
    }
}
