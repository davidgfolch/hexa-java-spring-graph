package com.dgf.java_spring_graph.application;

import com.dgf.java_spring_graph.domain.model.Product;
import com.dgf.java_spring_graph.domain.model.ProductInput;
import com.dgf.java_spring_graph.domain.port.ProductOperations;
import com.dgf.java_spring_graph.domain.port.ProductService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class ProductUseCases implements ProductOperations {

    private final ProductService service;

    public ProductUseCases(ProductService service) {
        this.service = service;
    }

    @Override
    public Mono<Product> createProduct(ProductInput input) {
        return service.createProduct(
                new Product(
                        UUID.randomUUID().toString(),
                        input.name(),
                        input.description(),
                        input.price(),
                        input.category()));
    }

    @Override
    public Mono<Product> getProductById(String id) {
        return service.getProductById(id);
    }

    @Override
    public Flux<Product> getAllProducts() {
        return service.getAllProducts();
    }

    @Override
    public Mono<Product> updateProduct(String id, ProductInput input) {
        return service.getProductById(id)
                .map(existing -> {
                    Product updated = existing;
                    if (input.name() != null && !input.name().isBlank()) {
                        updated = updated.withName(input.name());
                    }
                    if (input.description() != null) {
                        updated = updated.withDescription(input.description());
                    }
                    if (input.price() != null) {
                        updated = updated.withPrice(input.price());
                    }
                    if (input.category() != null && !input.category().isBlank()) {
                        updated = updated.withCategory(input.category());
                    }
                    return updated;
                })
                .flatMap(service::updateProduct);
    }

    @Override
    public Mono<Void> deleteProduct(String id) {
        return service.deleteProduct(id);
    }

    @Override
    public Flux<Product> getProductsByCategory(String category) {
        return service.getProductsByCategory(category);
    }
}