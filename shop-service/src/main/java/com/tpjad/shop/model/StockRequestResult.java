package com.tpjad.shop.model;

public record StockRequestResult(Status status, String message) {
    public enum Status {
        APPROVED,
        REJECTED,
        UNAVAILABLE,
        ERROR
    }
}
