package com.dgf.java_spring_graph.infrastructure.controller;

import com.dgf.java_spring_graph.domain.model.Product;
import com.dgf.java_spring_graph.domain.model.ProductInput;
import com.dgf.java_spring_graph.domain.port.ProductOperations;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class GraphQLProductController {

    private final ProductOperations operations;

    public GraphQLProductController(ProductOperations operations) {
        this.operations = operations;
    }

    @QueryMapping
    public Mono<Product> product(@Argument String id) {
        return operations.getProductById(id);
    }

    @QueryMapping
    public Flux<Product> products() {
        return operations.getAllProducts();
    }

    @QueryMapping
    public Flux<Product> productsByCategory(@Argument String category) {
        return operations.getProductsByCategory(category);
    }

    @MutationMapping
    public Mono<Product> createProduct(@Argument @Valid ProductInput input) {
        return operations.createProduct(input);
    }

    @MutationMapping
    public Mono<Product> updateProduct(@Argument String id, @Argument @Valid ProductInput input) {
        return operations.updateProduct(id, input);
    }

    @MutationMapping
    public Mono<Boolean> deleteProduct(@Argument String id) {
        return operations.deleteProduct(id)
                .thenReturn(true)
                .onErrorReturn(false);
    }
}