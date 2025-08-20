package com.example.PfaBack.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.PfaBack.models.Plant;

@Repository
public interface PlantRepository extends MongoRepository<Plant, String> {
    List<Plant> findByFarmId(String farmId);
}
