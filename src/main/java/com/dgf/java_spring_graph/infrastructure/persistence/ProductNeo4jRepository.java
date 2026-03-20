package com.dgf.java_spring_graph.infrastructure.persistence;

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import reactor.core.publisher.Flux;

public interface ProductNeo4jRepository extends ReactiveNeo4jRepository<ProductEntity, String> {

    Flux<ProductEntity> findByName(String name);

    Flux<ProductEntity> findByCategory(String category);
}