package com.dgf.java_spring_graph.infrastructure.adapter;

import com.dgf.java_spring_graph.domain.model.Product;
import com.dgf.java_spring_graph.domain.port.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceAdapterTest {

    @Mock
    private ProductRepository productRepository;

    private ProductServiceAdapter productServiceAdapter;

    @BeforeEach
    void setUp() {
        productServiceAdapter = new ProductServiceAdapter(productRepository);
    }

    @Test
    void testCreateProductSuccessfully() {
        // Given
        Product product = new Product(
            "1",
            "Test Product",
            "Description",
            new BigDecimal("99.99"),
            "Electronics"
        );
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(product));

        // When
        Mono<Product> result = productServiceAdapter.createProduct(product);

        // Then
        StepVerifier.create(result)
            .assertNext(p -> {
                assertEquals("1", p.id());
                assertEquals("Test Product", p.name());
            })
            .verifyComplete();

        verify(productRepository).save(product);
    }

    @Test
    void testGetProductByIdSuccessfully() {
        // Given
        Product product = new Product(
            "1",
            "Test Product",
            "Description",
            new BigDecimal("99.99"),
            "Electronics"
        );
        when(productRepository.findById("1")).thenReturn(Mono.just(product));

        // When
        Mono<Product> result = productServiceAdapter.getProductById("1");

        // Then
        StepVerifier.create(result)
            .assertNext(p -> {
                assertEquals("1", p.id());
                assertEquals("Test Product", p.name());
            })
            .verifyComplete();

        verify(productRepository).findById("1");
    }

    @Test
    void testGetProductByIdNotFound() {
        // Given
        when(productRepository.findById("999")).thenReturn(Mono.empty());

        // When
        Mono<Product> result = productServiceAdapter.getProductById("999");

        // Then
        StepVerifier.create(result)
            .verifyComplete();

        verify(productRepository).findById("999");
    }

    @Test
    void testGetAllProductsSuccessfully() {
        // Given
        Product product1 = new Product("1", "Product 1", "Desc 1", new BigDecimal("10.00"), "Category 1");
        Product product2 = new Product("2", "Product 2", "Desc 2", new BigDecimal("20.00"), "Category 2");
        when(productRepository.findAll()).thenReturn(Flux.just(product1, product2));

        // When
        Flux<Product> result = productServiceAdapter.getAllProducts();

        // Then
        StepVerifier.create(result)
            .assertNext(p -> assertEquals("1", p.id()))
            .assertNext(p -> assertEquals("2", p.id()))
            .verifyComplete();

        verify(productRepository).findAll();
    }

    @Test
    void testGetAllProductsEmpty() {
        // Given
        when(productRepository.findAll()).thenReturn(Flux.empty());

        // When
        Flux<Product> result = productServiceAdapter.getAllProducts();

        // Then
        StepVerifier.create(result)
            .verifyComplete();

        verify(productRepository).findAll();
    }

    @Test
    void testUpdateProductSuccessfully() {
        // Given
        Instant now = Instant.now();
        Product product = new Product(
            "1",
            "Updated Product",
            "Updated Description",
            new BigDecimal("199.99"),
            "Electronics",
            Instant.now(),
            now
        );

        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product savedProduct = invocation.getArgument(0);
            return Mono.just(savedProduct);
        });

        // When
        Mono<Product> result = productServiceAdapter.updateProduct(product);

        // Then
        StepVerifier.create(result)
            .assertNext(p -> {
                assertEquals("1", p.id());
                assertEquals("Updated Product", p.name());
                assertNotNull(p.updatedAt());
            })
            .verifyComplete();

        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testUpdateProductUpdatesTimestamp() {
        // Given
        Instant originalTime = Instant.parse("2023-01-01T00:00:00Z");
        Product product = new Product(
            "1",
            "Product",
            "Description",
            new BigDecimal("100.00"),
            "Category",
            originalTime,
            originalTime
        );

        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product savedProduct = invocation.getArgument(0);
            return Mono.just(savedProduct);
        });

        // When
        Mono<Product> result = productServiceAdapter.updateProduct(product);

        // Then
        StepVerifier.create(result)
            .assertNext(p -> {
                assertEquals(originalTime, p.createdAt());
                assertTrue(p.updatedAt().isAfter(originalTime));
            })
            .verifyComplete();
    }

    @Test
    void testDeleteProductSuccessfully() {
        // Given
        when(productRepository.deleteById("1")).thenReturn(Mono.empty());

        // When
        Mono<Void> result = productServiceAdapter.deleteProduct("1");

        // Then
        StepVerifier.create(result)
            .verifyComplete();

        verify(productRepository).deleteById("1");
    }

    @Test
    void testGetProductsByCategorySuccessfully() {
        // Given
        Product product1 = new Product("1", "Product 1", "Desc 1", new BigDecimal("10.00"), "Electronics");
        Product product2 = new Product("2", "Product 2", "Desc 2", new BigDecimal("20.00"), "Electronics");
        when(productRepository.findByCategory("Electronics")).thenReturn(Flux.just(product1, product2));

        // When
        Flux<Product> result = productServiceAdapter.getProductsByCategory("Electronics");

        // Then
        StepVerifier.create(result)
            .assertNext(p -> assertEquals("1", p.id()))
            .assertNext(p -> assertEquals("2", p.id()))
            .verifyComplete();

        verify(productRepository).findByCategory("Electronics");
    }

    @Test
    void testGetProductsByCategoryEmpty() {
        // Given
        when(productRepository.findByCategory("NonExistent")).thenReturn(Flux.empty());

        // When
        Flux<Product> result = productServiceAdapter.getProductsByCategory("NonExistent");

        // Then
        StepVerifier.create(result)
            .verifyComplete();

        verify(productRepository).findByCategory("NonExistent");
    }

    @Test
    void testGetProductByNameSuccessfully() {
        // Given
        Product product = new Product("1", "Unique Product", "Desc", new BigDecimal("50.00"), "Category");
        when(productRepository.findByName("Unique Product")).thenReturn(Mono.just(product));

        // When
        Mono<Product> result = productServiceAdapter.getProductByName("Unique Product");

        // Then
        StepVerifier.create(result)
            .assertNext(p -> {
                assertEquals("1", p.id());
                assertEquals("Unique Product", p.name());
            })
            .verifyComplete();

        verify(productRepository).findByName("Unique Product");
    }

    @Test
    void testGetProductByNameNotFound() {
        // Given
        when(productRepository.findByName("NonExistent")).thenReturn(Mono.empty());

        // When
        Mono<Product> result = productServiceAdapter.getProductByName("NonExistent");

        // Then
        StepVerifier.create(result)
            .verifyComplete();

        verify(productRepository).findByName("NonExistent");
    }
}
