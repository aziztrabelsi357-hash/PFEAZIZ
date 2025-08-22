package com.example.PfaBack.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.PfaBack.models.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
}
