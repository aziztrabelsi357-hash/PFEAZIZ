package com.example.PfaBack.controller;
import com.example.PfaBack.models.Disease;
import com.example.PfaBack.Services.DiseaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.PfaBack.models.Article;
@RestController

@RequestMapping("/diseases")
public class DiseaseController {
    @Autowired
    private DiseaseService diseaseService;
    @GetMapping("/test")
    public String test() {
        return "Public endpoint works!";
    }
    @GetMapping
    public List<Disease> getAllDiseases() {
        return diseaseService.getAllDiseases();
    }

    @GetMapping("/{id}")
    public Disease getDiseaseById(@PathVariable String id) {
        return diseaseService.getDiseaseById(id);
    }

    @PostMapping
    public Disease createDisease(@RequestBody Disease disease) {
        return diseaseService.createDisease(disease);
    }
    // Add a new article to a disease
    @PostMapping("/{diseaseId}/articles")
    public Disease addArticleToDisease(@PathVariable String diseaseId, @RequestBody Article article) {
        return diseaseService.addArticleToDisease(diseaseId, article);
    }
    // Get diseases by article title
    @GetMapping("/searchByTitle")
    public List<Disease> getDiseasesByArticleTitle(@RequestParam String title) {
        return diseaseService.getDiseasesByArticleTitle(title);
    }
    @PutMapping("/{id}")
    public Disease updateDisease(@PathVariable String id, @RequestBody Disease disease) {
        return diseaseService.updateDisease(id, disease);
    }

    @DeleteMapping("/{id}")
    public void deleteDisease(@PathVariable String id) {
        diseaseService.deleteDisease(id);
    }
    @GetMapping("/search")
    public List<Disease> searchDiseases(@RequestParam String keyword) {
        return diseaseService.searchDiseases(keyword);

    }
}

