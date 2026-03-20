package com.dgf.java_spring_graph.domain.port;

import com.dgf.java_spring_graph.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {
    Mono<Product> createProduct(Product product);
    Mono<Product> getProductById(String id);
    Flux<Product> getAllProducts();
    Mono<Product> updateProduct(Product product);
    Mono<Void> deleteProduct(String id);
    Flux<Product> getProductsByCategory(String category);
    Mono<Product> getProductByName(String name);
}