package com.order.order_service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.order_service.dto.request.OrderRequest;
import com.order.order_service.dto.response.OrderResponse;
import com.order.order_service.producer.OrderKafkaProducer;
import com.order.order_service.service.OrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;


@Service
public class OrderKafkaConsumer {

    private final OrderService orderService;
    private final OrderKafkaProducer orderKafkaProducer;
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderKafkaConsumer.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OrderKafkaConsumer(OrderService orderService, OrderKafkaProducer orderKafkaProducer) {
        this.orderService = orderService;
        this.orderKafkaProducer = orderKafkaProducer;
    }

    @KafkaListener(topics = "orders-request-topic", groupId = "order-consumer-group")
    public void consume(String orderMessage) throws JsonProcessingException, NoSuchAlgorithmException {
        LOGGER.info("Received message: {}", orderMessage);
        OrderRequest orderRequest = convertJsonToOrderRequest(orderMessage);

        try {
            // Processando o pedido
            OrderResponse orderResponse = orderService.processOrder(orderRequest);

            // Enviando a resposta do pedido e aguardando a confirmação de envio
            orderKafkaProducer.sendOrderProcessed(orderResponse).whenComplete((result, ex) -> {
                if (ex == null) {
                    LOGGER.info("Mensagem consumida e resposta enviada com sucesso.");
                } else {
                    LOGGER.error("Erro ao enviar a resposta do pedido: {}", ex.getMessage());
                    orderKafkaProducer.sendErrorMessage("Erro ao enviar a resposta do pedido: " + ex.getMessage());
                }
            });
        } catch (Exception e) {
            LOGGER.error("Erro ao processar pedido: {}", e.getMessage());
            orderKafkaProducer.sendErrorMessage("Erro ao processar pedido: " + e.getMessage());
        }
    }

    // Método para converter a mensagem JSON para um objeto OrderRequest
    private OrderRequest convertJsonToOrderRequest(String orderMessage) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(orderMessage, OrderRequest.class);
    }
}