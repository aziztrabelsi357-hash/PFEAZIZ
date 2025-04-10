package com.example.PfaBack.controller;

import com.example.PfaBack.models.Disease;
import com.example.PfaBack.Services.DiseaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/diseases")
public class AdminDiseaseController {

    @Autowired
    private DiseaseService diseaseService;

    @GetMapping
    public ResponseEntity<List<Disease>> getAllDiseases() {
        return ResponseEntity.ok(diseaseService.getAllDiseases());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Disease> getDiseaseById(@PathVariable String id) {
        Disease disease = diseaseService.getDiseaseById(id);
        if (disease == null) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok(disease);
    }

    @PostMapping
    public ResponseEntity<Disease> createDisease(@RequestBody Disease disease) {
        return ResponseEntity.ok(diseaseService.createDisease(disease));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Disease> updateDisease(@PathVariable String id, @RequestBody Disease disease) {
        Disease updatedDisease = diseaseService.updateDisease(id, disease);
        if (updatedDisease == null) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok(updatedDisease);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDisease(@PathVariable String id) {
        diseaseService.deleteDisease(id);
        return ResponseEntity.ok().build();
    }
}