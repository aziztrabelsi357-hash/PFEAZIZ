package com.example.PfaBack.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.PfaBack.models.Animal;
import com.example.PfaBack.repository.AnimalRepository;

@Service
public class AnimalService {
	private final AnimalRepository repository;

	public AnimalService(AnimalRepository repository) {
		this.repository = repository;
	}

	public List<Animal> getAllAnimals() {
		return repository.findAll();
	}

	public Animal getAnimalById(String id) {
		return repository.findById(id).orElse(null);
	}

	public Animal saveAnimal(Animal animal) {
		return repository.save(animal);
	}

	public void deleteAnimal(String id) {
		repository.deleteById(id);
	}
	public List<Animal> getAnimalsByFarmId(String farmId) {
		return repository.findByFarmId(farmId);
	}

	// Water intake tracking methods
	public Animal addIntake(String animalId, double liters) {
		return repository.findById(animalId).map(a -> {
			String today = java.time.LocalDate.now().toString();
			if (a.getIntakeUpdatedAt() == null || !a.getIntakeUpdatedAt().equals(today)) {
				a.setTodayIntakeLiters(0.0);
				a.setIntakeUpdatedAt(today);
			}
			double current = a.getTodayIntakeLiters() != null ? a.getTodayIntakeLiters() : 0.0;
			a.setTodayIntakeLiters(current + liters);
			return repository.save(a);
		}).orElseThrow(() -> new RuntimeException("Animal not found: " + animalId));
	}

	public Animal setRecommendedIntake(String animalId, double liters) {
		return repository.findById(animalId).map(a -> {
			a.setRecommendedIntakeLiters(liters);
			return repository.save(a);
		}).orElseThrow(() -> new RuntimeException("Animal not found: " + animalId));
	}

	public Animal resetIntake(String animalId) {
		return repository.findById(animalId).map(a -> {
			a.setTodayIntakeLiters(0.0);
			a.setIntakeUpdatedAt(java.time.LocalDate.now().toString());
			return repository.save(a);
		}).orElseThrow(() -> new RuntimeException("Animal not found: " + animalId));
	}
  
}
