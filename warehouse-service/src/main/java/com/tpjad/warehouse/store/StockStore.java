package com.tpjad.warehouse.store;

import com.tpjad.warehouse.model.Flower;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class StockStore {
    private static final StockStore INSTANCE = new StockStore();
    private final Map<String, Flower> stock = new ConcurrentHashMap<>();

    private StockStore() {
    }

    public static StockStore getInstance() {
        return INSTANCE;
    }

    public synchronized void loadFromCsv(InputStream input) throws IOException {
        if (input == null) {
            throw new IOException("Stock CSV resource was not found");
        }
        Map<String, Flower> loaded = new ConcurrentHashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) {
                    continue;
                }
                String[] fields = line.split(",", -1);
                if (fields.length != 3) {
                    throw new IOException("Invalid CSV at line " + lineNumber);
                }
                String flowerId = fields[0].trim();
                String name = fields[1].trim();
                if (flowerId.isEmpty() || name.isEmpty()) {
                    throw new IOException("Invalid CSV at line " + lineNumber);
                }
                int quantity;
                try {
                    quantity = Integer.parseInt(fields[2].trim());
                } catch (NumberFormatException exception) {
                    throw new IOException("Invalid quantity at line " + lineNumber, exception);
                }
                if (quantity < 0 || loaded.put(flowerId, new Flower(flowerId, name, quantity)) != null) {
                    throw new IOException("Invalid or duplicate flower at line " + lineNumber);
                }
            }
        }
        if (loaded.isEmpty()) {
            throw new IOException("Stock CSV is empty");
        }
        stock.clear();
        stock.putAll(loaded);
    }

    public List<Flower> findAll() {
        List<Flower> result = stock.values().stream()
                .map(flower -> new Flower(flower.getFlowerId(), flower.getName(), flower.getQuantity()))
                .sorted(Comparator.comparing(Flower::getFlowerId))
                .toList();
        return new ArrayList<>(result);
    }

    public synchronized boolean approveRequest(String flowerId, int requestedQuantity) {
        Flower flower = stock.get(flowerId);
        if (flower == null || requestedQuantity <= 0 || flower.getQuantity() < requestedQuantity) {
            return false;
        }
        flower.setQuantity(flower.getQuantity() - requestedQuantity);
        return true;
    }
}
