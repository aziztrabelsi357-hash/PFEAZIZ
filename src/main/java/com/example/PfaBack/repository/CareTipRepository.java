package com.example.PfaBack.repository;

import com.example.PfaBack.models.CareTip;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CareTipRepository extends MongoRepository<CareTip, String> {
}
