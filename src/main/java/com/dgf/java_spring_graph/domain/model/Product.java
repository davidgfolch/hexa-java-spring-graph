package com.dgf.java_spring_graph.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.Instant;

public record Product(
    @NotBlank(message = "Product id is required")
    String id,

    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must not exceed 255 characters")
    String name,

    @Size(max = 2000, message = "Product description must not exceed 2000 characters")
    String description,

    @NotNull(message = "Product price is required")
    @Positive(message = "Product price must be positive")
    BigDecimal price,

    @Size(max = 100, message = "Product category must not exceed 100 characters")
    String category,

    Instant createdAt,
    Instant updatedAt
) {
    public Product {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Product id cannot be null or blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be null or blank");
        }
    }

    public Product(String id, String name, String description, BigDecimal price, String category) {
        this(id, name, description, price, category, Instant.now(), Instant.now());
    }

    public Product withId(String id) {
        return new Product(id, name, description, price, category, createdAt, updatedAt);
    }

    public Product withName(String name) {
        return new Product(id, name, description, price, category, createdAt, Instant.now());
    }

    public Product withDescription(String description) {
        return new Product(id, name, description, price, category, createdAt, Instant.now());
    }

    public Product withPrice(BigDecimal price) {
        return new Product(id, name, description, price, category, createdAt, Instant.now());
    }

    public Product withCategory(String category) {
        return new Product(id, name, description, price, category, createdAt, Instant.now());
    }

    public Product withUpdatedAt(Instant updatedAt) {
        return new Product(id, name, description, price, category, createdAt, updatedAt);
    }
}