package com.example.PfaBack.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.PfaBack.models.Farm;

@Repository
public interface FarmRepository extends MongoRepository<Farm, String> {

    @Override
    Optional<Farm> findById(String id);

    Optional<Farm> findByUserId(String userId);
}
