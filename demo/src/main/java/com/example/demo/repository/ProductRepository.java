package com.example.demo.repository;

import com.example.demo.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryIgnoreCase(String category);

    List <Product> findByNameContainingIgnoreCase(String name);

    Optional <Product> findTopByPriceAsc();

    Optional<Product> fin

}
