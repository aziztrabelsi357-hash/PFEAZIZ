package com.example.PfaBack.repository;
import com.example.PfaBack.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    // removed disease-related query because Product has no diseaseId field
}
