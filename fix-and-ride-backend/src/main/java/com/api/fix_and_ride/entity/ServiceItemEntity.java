package com.api.fix_and_ride.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "services")
public class ServiceItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "key_name", unique = true, nullable = false, length = 50)
    private String keyName;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, columnDefinition = "DOUBLE DEFAULT 0.0")
    private double price = 0.0; // default value

    @Column(length = 1000)
    private String description;

    // Required no-arg constructor for JPA
    public ServiceItemEntity() {}

    // Convenience constructor
    public ServiceItemEntity(String keyName, String name, String description) {
        this.keyName = keyName;
        this.name = name;
        this.description = description;
    }

    public ServiceItemEntity(String keyName, String name, double price, String description) {
        this.keyName = keyName;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getKeyName() { return keyName; }
    public void setKeyName(String keyName) { this.keyName = keyName; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "ServiceItemEntity{" +
                "id=" + id +
                ", keyName='" + keyName + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                '}';
    }
}
