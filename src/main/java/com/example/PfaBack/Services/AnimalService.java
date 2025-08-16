

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
}
