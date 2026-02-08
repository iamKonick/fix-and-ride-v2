package com.api.fix_and_ride.dto;

public class ServiceItemResponseDTO {

    private String keyName;
    private String name;
    private double price;
    private String description;

    public ServiceItemResponseDTO() {}

    public ServiceItemResponseDTO(String keyName, String name, double price, String description) {
        this.keyName = keyName;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
// getters & setters
}
