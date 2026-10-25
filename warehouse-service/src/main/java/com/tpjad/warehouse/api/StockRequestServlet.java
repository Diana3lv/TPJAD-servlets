package com.tpjad.warehouse.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.tpjad.warehouse.store.StockStore;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/stock-requests")
public class StockRequestServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            JsonNode body = objectMapper.readTree(request.getReader());
            if (body == null || !body.isObject() || !body.hasNonNull("flowerId")
                    || !body.get("flowerId").isTextual() || !body.has("quantity")
                    || !body.get("quantity").isIntegralNumber()
                    || !body.get("quantity").canConvertToInt()) {
                sendBadRequest(response, "Request must contain a textual flowerId and an integer quantity.");
                return;
            }
            String flowerId = body.get("flowerId").asText().trim();
            int quantity = body.get("quantity").asInt();
            if (flowerId.isEmpty() || quantity <= 0) {
                sendBadRequest(response, "Quantity must be greater than zero and flowerId must not be empty.");
                return;
            }
            boolean approved = StockStore.getInstance().approveRequest(flowerId, quantity);
            objectMapper.writeValue(response.getWriter(), new StatusResponse(approved ? "APPROVED" : "REJECTED"));
        } catch (JsonProcessingException exception) {
            sendBadRequest(response, "Request body is not valid JSON.");
        }
    }

    private void sendBadRequest(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        objectMapper.writeValue(response.getWriter(), new ErrorResponse("INVALID_REQUEST", message));
    }

    private record StatusResponse(String status) {
    }

    private record ErrorResponse(String status, String message) {
    }
}
