package com.dgf.java_spring_graph.application;

import com.dgf.java_spring_graph.domain.model.Product;
import com.dgf.java_spring_graph.domain.model.ProductInput;
import com.dgf.java_spring_graph.domain.port.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductUseCasesTest {

    @Mock
    private ProductService productService;

    private ProductUseCases productUseCases;

    @BeforeEach
    void setUp() {
        productUseCases = new ProductUseCases(productService);
    }

    @Test
    void testCreateProductSuccessfully() {
        // Given
        ProductInput input = new ProductInput("Test Product", "A test product", new BigDecimal("99.99"), "Electronics");

        when(productService.createProduct(any(Product.class))).thenAnswer(invocation -> {
            Product productArg = invocation.getArgument(0);
            return Mono.just(productArg);
        });

        // When
        Mono<Product> result = productUseCases.createProduct(input);

        // Then
        StepVerifier.create(result)
            .assertNext(p -> {
                assertNotNull(p.id());
                assertEquals("Test Product", p.name());
                assertEquals("A test product", p.description());
                assertEquals(new BigDecimal("99.99"), p.price());
                assertEquals("Electronics", p.category());
            })
            .verifyComplete();

        verify(productService).createProduct(any(Product.class));
    }

    @Test
    void testCreateProductWithNullDescription() {
        // Given
        ProductInput input = new ProductInput("Product", null, new BigDecimal("50.00"), "Books");

        when(productService.createProduct(any(Product.class))).thenAnswer(invocation -> {
            Product productArg = invocation.getArgument(0);
            return Mono.just(productArg);
        });

        // When
        Mono<Product> result = productUseCases.createProduct(input);

        // Then
        StepVerifier.create(result)
            .assertNext(p -> {
                assertEquals("Product", p.name());
                assertNull(p.description());
            })
            .verifyComplete();
    }

    @Test
    void testGetProductByIdSuccessfully() {
        // Given
        Product product = new Product("1", "Test Product", "Description", new BigDecimal("99.99"), "Electronics");
        when(productService.getProductById("1")).thenReturn(Mono.just(product));

        // When
        Mono<Product> result = productUseCases.getProductById("1");

        // Then
        StepVerifier.create(result)
            .assertNext(p -> {
                assertEquals("1", p.id());
                assertEquals("Test Product", p.name());
            })
            .verifyComplete();

        verify(productService).getProductById("1");
    }

    @Test
    void testGetProductByIdNotFound() {
        // Given
        when(productService.getProductById("999")).thenReturn(Mono.empty());

        // When
        Mono<Product> result = productUseCases.getProductById("999");

        // Then
        StepVerifier.create(result)
            .verifyComplete();
    }

    @Test
    void testGetAllProductsSuccessfully() {
        // Given
        Product product1 = new Product("1", "Product 1", "Desc 1", new BigDecimal("10.00"), "Category 1");
        Product product2 = new Product("2", "Product 2", "Desc 2", new BigDecimal("20.00"), "Category 2");
        when(productService.getAllProducts()).thenReturn(Flux.just(product1, product2));

        // When
        Flux<Product> result = productUseCases.getAllProducts();

        // Then
        StepVerifier.create(result)
            .assertNext(p -> assertEquals("1", p.id()))
            .assertNext(p -> assertEquals("2", p.id()))
            .verifyComplete();

        verify(productService).getAllProducts();
    }

    @Test
    void testGetAllProductsEmpty() {
        // Given
        when(productService.getAllProducts()).thenReturn(Flux.empty());

        // When
        Flux<Product> result = productUseCases.getAllProducts();

        // Then
        StepVerifier.create(result)
            .verifyComplete();
    }

    @Test
    void testUpdateProductAllFieldsSuccessfully() {
        // Given
        String productId = "1";
        ProductInput input = new ProductInput("Updated Name", "Updated Description", new BigDecimal("199.99"), "Updated Category");
        Product existing = new Product("1", "Old Name", "Old Description", new BigDecimal("99.99"), "Old Category");
        Product updated = new Product("1", "Updated Name", "Updated Description", new BigDecimal("199.99"), "Updated Category");

        when(productService.getProductById(productId)).thenReturn(Mono.just(existing));
        when(productService.updateProduct(any(Product.class))).thenReturn(Mono.just(updated));

        // When
        Mono<Product> result = productUseCases.updateProduct(productId, input);

        // Then
        StepVerifier.create(result)
            .assertNext(p -> {
                assertEquals("Updated Name", p.name());
                assertEquals("Updated Description", p.description());
                assertEquals(new BigDecimal("199.99"), p.price());
                assertEquals("Updated Category", p.category());
            })
            .verifyComplete();

        verify(productService).getProductById(productId);
        verify(productService).updateProduct(any(Product.class));
    }

    @Test
    void testUpdateProductOnlyNameSuccessfully() {
        // Given
        String productId = "1";
        ProductInput input = new ProductInput("New Name", null, null, null);
        Product existing = new Product("1", "Old Name", "Old Description", new BigDecimal("99.99"), "Old Category");
        Product expected = new Product("1", "New Name", "Old Description", new BigDecimal("99.99"), "Old Category");

        when(productService.getProductById(productId)).thenReturn(Mono.just(existing));
        when(productService.updateProduct(any(Product.class))).thenReturn(Mono.just(expected));

        // When
        Mono<Product> result = productUseCases.updateProduct(productId, input);

        // Then
        StepVerifier.create(result)
            .assertNext(p -> {
                assertEquals("New Name", p.name());
                assertEquals("Old Description", p.description());
            })
            .verifyComplete();
    }

    @Test
    void testUpdateProductIgnoresBlankName() {
        // Given
        String productId = "1";
        ProductInput input = new ProductInput("   ", "New Description", new BigDecimal("150.00"), null);
        Product existing = new Product("1", "Old Name", "Old Description", new BigDecimal("99.99"), "Old Category");
        Product expected = new Product("1", "Old Name", "New Description", new BigDecimal("150.00"), "Old Category");

        when(productService.getProductById(productId)).thenReturn(Mono.just(existing));
        when(productService.updateProduct(any(Product.class))).thenReturn(Mono.just(expected));

        // When
        Mono<Product> result = productUseCases.updateProduct(productId, input);

        // Then
        StepVerifier.create(result)
            .assertNext(p -> {
                assertEquals("Old Name", p.name()); // Name should not be updated
                assertEquals("New Description", p.description());
            })
            .verifyComplete();
    }

    @Test
    void testUpdateProductIgnoresBlankCategory() {
        // Given
        String productId = "1";
        ProductInput input = new ProductInput(null, null, null, "   ");
        Product existing = new Product("1", "Name", "Description", new BigDecimal("99.99"), "OldCategory");
        Product expected = new Product("1", "Name", "Description", new BigDecimal("99.99"), "OldCategory");

        when(productService.getProductById(productId)).thenReturn(Mono.just(existing));
        when(productService.updateProduct(any(Product.class))).thenReturn(Mono.just(expected));

        // When
        Mono<Product> result = productUseCases.updateProduct(productId, input);

        // Then
        StepVerifier.create(result)
            .assertNext(p -> {
                assertEquals("OldCategory", p.category()); // Category should not be updated
            })
            .verifyComplete();
    }

    @Test
    void testUpdateProductNotFound() {
        // Given
        String productId = "999";
        ProductInput input = new ProductInput("Name", "Description", new BigDecimal("10.00"), "Category");
        when(productService.getProductById(productId)).thenReturn(Mono.empty());

        // When
        Mono<Product> result = productUseCases.updateProduct(productId, input);

        // Then
        StepVerifier.create(result)
            .verifyComplete();
    }

    @Test
    void testDeleteProductSuccessfully() {
        // Given
        when(productService.deleteProduct("1")).thenReturn(Mono.empty());

        // When
        Mono<Void> result = productUseCases.deleteProduct("1");

        // Then
        StepVerifier.create(result)
            .verifyComplete();

        verify(productService).deleteProduct("1");
    }

    @Test
    void testGetProductsByCategorySuccessfully() {
        // Given
        Product product1 = new Product("1", "Product 1", "Desc 1", new BigDecimal("10.00"), "Electronics");
        Product product2 = new Product("2", "Product 2", "Desc 2", new BigDecimal("20.00"), "Electronics");
        when(productService.getProductsByCategory("Electronics")).thenReturn(Flux.just(product1, product2));

        // When
        Flux<Product> result = productUseCases.getProductsByCategory("Electronics");

        // Then
        StepVerifier.create(result)
            .assertNext(p -> assertEquals("1", p.id()))
            .assertNext(p -> assertEquals("2", p.id()))
            .verifyComplete();

        verify(productService).getProductsByCategory("Electronics");
    }

    @Test
    void testGetProductsByCategoryEmpty() {
        // Given
        when(productService.getProductsByCategory("NonExistent")).thenReturn(Flux.empty());

        // When
        Flux<Product> result = productUseCases.getProductsByCategory("NonExistent");

        // Then
        StepVerifier.create(result)
            .verifyComplete();
    }
}
