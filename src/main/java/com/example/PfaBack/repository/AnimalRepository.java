package com.example.PfaBack.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.PfaBack.models.Animal;

public interface AnimalRepository extends MongoRepository<Animal, String> {
    List<Animal> findByFarmId(String farmId);
}