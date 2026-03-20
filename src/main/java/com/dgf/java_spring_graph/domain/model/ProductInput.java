package com.dgf.java_spring_graph.domain.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Input for creating or updating a product.
 * This is the inbound port object - the external contract for product operations.
 */
public record ProductInput(
    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must not exceed 255 characters")
    String name,

    @Size(max = 2000, message = "Product description must not exceed 2000 characters")
    String description,

    @NotNull(message = "Product price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Product price must be positive")
    BigDecimal price,

    @Size(max = 100, message = "Product category must not exceed 100 characters")
    String category
) {}