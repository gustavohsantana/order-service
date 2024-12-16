package com.order.order_service.controller;

import com.order.order_service.dto.request.OrderRequest;
import com.order.order_service.dto.response.OrderResponse;
import com.order.order_service.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/orders", produces = {"application/json"})
@Tag(name = "order-service", description = "APIs para gerenciamento de pedidos")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "Criar um pedido", description = "Cria um novo pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public OrderResponse createOrder(@RequestBody OrderRequest orderRequest) throws NoSuchAlgorithmException {
        return orderService.processOrder(orderRequest);
    }

    @Operation(summary = "Listar todos os pedidos", description = "Lista todos os pedidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos listados com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/list")
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    @Operation(summary = "Buscar pedido por ID", description = "Busca um pedido pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/get/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        Optional<OrderResponse> orderResponse = Optional.ofNullable(orderService.getOrderById(id));
        return orderResponse.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}