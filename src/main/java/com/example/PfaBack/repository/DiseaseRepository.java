package com.example.PfaBack.repository;

import com.example.PfaBack.models.Disease;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiseaseRepository extends MongoRepository<Disease, String> {
    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<Disease> findByNameContaining(String keyword);

}
