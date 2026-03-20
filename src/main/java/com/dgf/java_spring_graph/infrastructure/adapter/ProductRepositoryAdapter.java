package com.dgf.java_spring_graph.infrastructure.adapter;

import com.dgf.java_spring_graph.domain.model.Product;
import com.dgf.java_spring_graph.domain.port.ProductRepository;
import com.dgf.java_spring_graph.infrastructure.persistence.ProductMapper;
import com.dgf.java_spring_graph.infrastructure.persistence.ProductNeo4jRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ProductRepositoryAdapter implements ProductRepository {

    private final ProductNeo4jRepository repo;
    private final ProductMapper mapper;

    public ProductRepositoryAdapter(ProductNeo4jRepository repo, ProductMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public Mono<Product> save(Product product) {
        return repo.save(mapper.toNewEntity(product))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Product> findById(String id) {
        return repo.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Product> findAll() {
        return repo.findAll()
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Product> findByName(String name) {
        return repo.findByName(name)
                .singleOrEmpty()
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repo.deleteById(id);
    }

    @Override
    public Flux<Product> findByCategory(String category) {
        return repo.findByCategory(category)
                .map(mapper::toDomain);
    }
}