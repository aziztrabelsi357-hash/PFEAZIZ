package com.example.PfaBack.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.PfaBack.models.Animal;

public interface AnimalRepository extends MongoRepository<Animal, String> {}
