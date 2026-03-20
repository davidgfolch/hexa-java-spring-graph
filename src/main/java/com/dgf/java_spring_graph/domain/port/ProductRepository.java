package com.dgf.java_spring_graph.domain.port;

import com.dgf.java_spring_graph.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {
    Mono<Product> save(Product product);
    Mono<Product> findById(String id);
    Flux<Product> findAll();
    Mono<Product> findByName(String name);
    Mono<Void> deleteById(String id);
    Flux<Product> findByCategory(String category);
}