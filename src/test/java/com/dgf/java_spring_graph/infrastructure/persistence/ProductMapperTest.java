package com.dgf.java_spring_graph.infrastructure.persistence;

import com.dgf.java_spring_graph.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    private ProductMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(ProductMapper.class);
    }

    @Test
    void testToDomainWithValidEntity() {
        // Given
        ProductEntity entity = new ProductEntity();
        entity.setId("1");
        entity.setName("Test Product");
        entity.setDescription("A test product");
        entity.setPrice(new BigDecimal("99.99"));
        entity.setCategory("Electronics");
        Instant now = Instant.now();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        // When
        Product product = mapper.toDomain(entity);

        // Then
        assertNotNull(product);
        assertEquals("1", product.id());
        assertEquals("Test Product", product.name());
        assertEquals("A test product", product.description());
        assertEquals(new BigDecimal("99.99"), product.price());
        assertEquals("Electronics", product.category());
        assertEquals(now, product.createdAt());
        assertEquals(now, product.updatedAt());
    }

    @Test
    void testToDomainWithNullEntity() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    void testToEntityWithValidProduct() {
        // Given
        Instant now = Instant.now();
        Product product = new Product(
            "1",
            "Test Product",
            "A test product",
            new BigDecimal("99.99"),
            "Electronics",
            now,
            now
        );

        // When
        ProductEntity entity = mapper.toEntity(product);

        // Then
        assertNotNull(entity);
        assertEquals("1", entity.getId());
        assertEquals("Test Product", entity.getName());
        assertEquals("A test product", entity.getDescription());
        assertEquals(new BigDecimal("99.99"), entity.getPrice());
        assertEquals("Electronics", entity.getCategory());
        assertEquals(now, entity.getCreatedAt());
        assertEquals(now, entity.getUpdatedAt());
    }

    @Test
    void testToEntityWithNullProduct() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void testToNewEntityWithValidProduct() {
        // Given
        Product product = new Product(
            "1",
            "New Product",
            "A new test product",
            new BigDecimal("49.99"),
            "Books"
        );

        // When
        ProductEntity entity = mapper.toNewEntity(product);

        // Then
        assertNotNull(entity);
        assertNull(entity.getId()); // ID should not be set for new entity
        assertEquals("New Product", entity.getName());
        assertEquals("A new test product", entity.getDescription());
        assertEquals(new BigDecimal("49.99"), entity.getPrice());
        assertEquals("Books", entity.getCategory());
        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());
        assertEquals(entity.getCreatedAt(), entity.getUpdatedAt());
    }

    @Test
    void testToNewEntityWithNullProduct() {
        assertNull(mapper.toNewEntity(null));
    }

    @Test
    void testToNewEntitySetsCurrentTimestamps() {
        // Given
        Instant beforeCreation = Instant.now();
        Product product = new Product(
            "1",
            "Product",
            "Description",
            new BigDecimal("10.00"),
            "Category"
        );

        // When
        ProductEntity entity = mapper.toNewEntity(product);
        Instant afterCreation = Instant.now();

        // Then
        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());
        assertTrue(entity.getCreatedAt().isAfter(beforeCreation) || entity.getCreatedAt().equals(beforeCreation));
        assertTrue(entity.getCreatedAt().isBefore(afterCreation) || entity.getCreatedAt().equals(afterCreation));
    }
}
