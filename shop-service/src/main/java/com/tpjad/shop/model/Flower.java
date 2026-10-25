package com.tpjad.shop.model;

public class Flower {
    private String flowerId;
    private String name;
    private int quantity;

    public Flower() {
    }

    public Flower(String flowerId, String name, int quantity) {
        this.flowerId = flowerId;
        this.name = name;
        this.quantity = quantity;
    }

    public String getFlowerId() {
        return flowerId;
    }

    public void setFlowerId(String flowerId) {
        this.flowerId = flowerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
