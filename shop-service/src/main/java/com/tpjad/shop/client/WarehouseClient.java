package com.tpjad.shop.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.tpjad.shop.model.StockRequestResult;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class WarehouseClient {
    private static final URI REQUEST_URI = URI.create("http://localhost:8081/warehouse/api/stock-requests");
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(2))
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public StockRequestResult requestStock(String flowerId, int quantity) {
        try {
            String body = objectMapper.createObjectNode()
                    .put("flowerId", flowerId)
                    .put("quantity", quantity)
                    .toString();
            HttpRequest request = HttpRequest.newBuilder(REQUEST_URI)
                    .timeout(Duration.ofSeconds(5))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                return new StockRequestResult(StockRequestResult.Status.ERROR,
                        "Warehouse returned HTTP " + response.statusCode());
            }
            JsonNode responseBody;
            try {
                responseBody = objectMapper.readTree(response.body());
            } catch (JsonProcessingException exception) {
                return new StockRequestResult(StockRequestResult.Status.ERROR,
                        "Warehouse returned an invalid response");
            }
            if (responseBody == null || !responseBody.has("status")
                    || !responseBody.get("status").isTextual()) {
                return new StockRequestResult(StockRequestResult.Status.ERROR,
                        "Warehouse returned an invalid response");
            }
            return switch (responseBody.get("status").asText()) {
                case "APPROVED" -> new StockRequestResult(StockRequestResult.Status.APPROVED, "Stock request approved");
                case "REJECTED" -> new StockRequestResult(StockRequestResult.Status.REJECTED, "Stock request rejected");
                default -> new StockRequestResult(StockRequestResult.Status.ERROR,
                        "Warehouse returned an unexpected status");
            };
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            return new StockRequestResult(StockRequestResult.Status.UNAVAILABLE, "Warehouse is unavailable");
        } catch (IOException exception) {
            return new StockRequestResult(StockRequestResult.Status.UNAVAILABLE, "Warehouse is unavailable");
        }
    }
}
