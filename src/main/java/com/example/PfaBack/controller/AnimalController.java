package com.example.PfaBack.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.PfaBack.Services.AnimalService;
import com.example.PfaBack.models.Animal;

@RestController
@RequestMapping("/api/animals")
public class AnimalController {
	private final AnimalService animalService;

	public AnimalController(AnimalService animalService) {
		this.animalService = animalService;
	}

	@GetMapping
	public List<Animal> getAllAnimals() {
		return animalService.getAllAnimals();
	}

	@GetMapping("/{id}")
	public Animal getAnimalById(@PathVariable String id) {
		return animalService.getAnimalById(id);
	}

	@PostMapping
	public Animal addAnimal(@RequestBody Animal animal) {
		return animalService.saveAnimal(animal);
	}

	@PutMapping("/{id}")
	public Animal updateAnimal(@PathVariable String id, @RequestBody Animal animal) {
		animal.setId(id);
		return animalService.saveAnimal(animal);
	}

	@DeleteMapping("/{id}")
	public void deleteAnimal(@PathVariable String id) {
		animalService.deleteAnimal(id);
	}
}
