package com.historias_de_cafe.backend.repository;

import com.historias_de_cafe.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    List<Product> findByActiveTrue();
    List<Product> findByActive(boolean active);
}
