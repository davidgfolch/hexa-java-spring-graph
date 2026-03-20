package com.dgf.java_spring_graph.infrastructure.config;

import com.dgf.java_spring_graph.application.ProductUseCases;
import com.dgf.java_spring_graph.domain.port.ProductOperations;
import com.dgf.java_spring_graph.domain.port.ProductRepository;
import com.dgf.java_spring_graph.domain.port.ProductService;
import com.dgf.java_spring_graph.infrastructure.adapter.ProductRepositoryAdapter;
import com.dgf.java_spring_graph.infrastructure.adapter.ProductServiceAdapter;
import com.dgf.java_spring_graph.infrastructure.persistence.ProductMapper;
import com.dgf.java_spring_graph.infrastructure.persistence.ProductNeo4jRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableReactiveNeo4jRepositories;

@Configuration
@EnableReactiveNeo4jRepositories(basePackages = "com.dgf.java_spring_graph.infrastructure.persistence")
public class ProductConfig {

    @Bean
    public ProductRepository productRepository(ProductNeo4jRepository neo4jRepository, ProductMapper mapper) {
        return new ProductRepositoryAdapter(neo4jRepository, mapper);
    }

    @Bean
    public ProductService productService(ProductRepository productRepository) {
        return new ProductServiceAdapter(productRepository);
    }

    @Bean
    public ProductOperations productOperations(ProductService productService) {
        return new ProductUseCases(productService);
    }
}