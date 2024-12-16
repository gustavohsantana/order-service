package com.order.order_service.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.order_service.dto.response.OrderResponse;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class OrderKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServers;

    public OrderKafkaProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    // Método para enviar a resposta do pedido processado para o Kafka com callback assíncrono
    public CompletableFuture<Void> sendOrderProcessed(OrderResponse orderResponse) throws JsonProcessingException {
        String orderMessage = convertOrderResponseToJson(orderResponse);
        CompletableFuture<Void> future = new CompletableFuture<>();

        kafkaTemplate.send("orders-response-topic", orderMessage).whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Mensagem enviada com sucesso: " + result.getRecordMetadata());
                future.complete(null); // Completa a execução
            } else {
                System.err.println("Erro ao enviar mensagem: " + ex.getMessage());
                future.completeExceptionally(ex); // Completa com erro
            }
        });

        return future;
    }

    // Método para enviar mensagens de erro para o Kafka
    public void sendErrorMessage(String errorMessage) {
        kafkaTemplate.send("orders-error-topic", errorMessage);
    }

    // Método para converter o OrderResponse para uma string JSON
    private String convertOrderResponseToJson(OrderResponse orderResponse) throws JsonProcessingException {
        return objectMapper.writeValueAsString(orderResponse);
    }
}