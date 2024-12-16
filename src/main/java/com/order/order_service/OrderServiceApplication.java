package com.order.order_service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Order Service", version = "1", description = "Service para gestao de pedidos from Service A to Service B"))
public class OrderServiceApplication {

	public static void main(String[] args) {
		// Inicia o Spring Boot
		ApplicationContext context = SpringApplication.run(OrderServiceApplication.class, args);
	}
}
