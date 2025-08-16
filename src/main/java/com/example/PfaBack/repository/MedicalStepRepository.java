package com.example.PfaBack.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.PfaBack.models.MedicalStep;


public interface MedicalStepRepository extends MongoRepository<MedicalStep, String> {}
