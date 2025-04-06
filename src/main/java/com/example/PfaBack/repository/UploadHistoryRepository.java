package com.example.PfaBack.repository;

import com.example.PfaBack.models.UploadHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UploadHistoryRepository extends MongoRepository<UploadHistory, String> {
    List<UploadHistory> findByUserId(String userId);
}