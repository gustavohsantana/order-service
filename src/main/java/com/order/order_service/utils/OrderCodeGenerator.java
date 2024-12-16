package com.order.order_service.utils;

import com.order.order_service.dto.request.OrderRequest;
import com.order.order_service.dto.request.ProductRequest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class OrderCodeGenerator {

    /**
     * Gera um orderCode único com base nos dados do pedido.
     *
     * @param orderRequest Objeto OrderRequest contendo os dados do pedido
     * @return Um código único de pedido gerado
     * @throws NoSuchAlgorithmException Caso o algoritmo de hash não esteja disponível
     */
    public static String generateOrderCode(OrderRequest orderRequest) throws NoSuchAlgorithmException {
        // Cria uma StringBuilder para concatenar os dados
        StringBuilder sb = new StringBuilder();

        // Adiciona os dados do pedido (para garantir unicidade)
        sb.append(orderRequest.getCustomerId());

        // Ordena os produtos por ID para garantir que a ordem não afete o código
        List<ProductRequest> products = orderRequest.getProducts();
        products.stream()
                .sorted((p1, p2) -> Integer.compare(p1.getProductId(), p2.getProductId()))
                .forEach(product -> {
                    sb.append(product.getProductId());
                    sb.append(product.getQuantity());
                    sb.append(product.getPrice());
                });

        // Gera o hash a partir da chave concatenada
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(sb.toString().getBytes());

        // Converte o hash para uma string hexadecimal
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }

        // Retorna o código gerado
        return hexString.toString();
    }
}