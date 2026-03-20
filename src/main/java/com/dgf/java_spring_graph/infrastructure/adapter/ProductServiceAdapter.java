package com.dgf.java_spring_graph.infrastructure.adapter;

import com.dgf.java_spring_graph.domain.model.Product;
import com.dgf.java_spring_graph.domain.port.ProductRepository;
import com.dgf.java_spring_graph.domain.port.ProductService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

public class ProductServiceAdapter implements ProductService {

    private final ProductRepository repo;

    public ProductServiceAdapter(ProductRepository repo) {
        this.repo = repo;
    }

    @Override
    public Mono<Product> createProduct(Product product) {
        return repo.save(product);
    }

    @Override
    public Mono<Product> getProductById(String id) {
        return repo.findById(id);
    }

    @Override
    public Flux<Product> getAllProducts() {
        return repo.findAll();
    }

    @Override
    public Mono<Product> updateProduct(Product product) {
        return repo.save(product.withUpdatedAt(Instant.now()));
    }

    @Override
    public Mono<Void> deleteProduct(String id) {
        return repo.deleteById(id);
    }

    @Override
    public Flux<Product> getProductsByCategory(String category) {
        return repo.findByCategory(category);
    }

    @Override
    public Mono<Product> getProductByName(String name) {
        return repo.findByName(name);
    }
}