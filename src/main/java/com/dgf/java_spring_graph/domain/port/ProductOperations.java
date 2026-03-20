package com.dgf.java_spring_graph.domain.port;

import com.dgf.java_spring_graph.domain.model.Product;
import com.dgf.java_spring_graph.domain.model.ProductInput;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Inbound port for product operations.
 * Defines the operations that external adapters (like GraphQL controller) can invoke.
 */
public interface ProductOperations {
    Mono<Product> createProduct(ProductInput input);
    Mono<Product> getProductById(String id);
    Flux<Product> getAllProducts();
    Mono<Product> updateProduct(String id, ProductInput input);
    Mono<Void> deleteProduct(String id);
    Flux<Product> getProductsByCategory(String category);
}