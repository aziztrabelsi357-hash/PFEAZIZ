package com.example.PfaBack.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.PfaBack.models.MedicalProcess;

public interface MedicalProcessRepository extends MongoRepository<MedicalProcess, String> {}
