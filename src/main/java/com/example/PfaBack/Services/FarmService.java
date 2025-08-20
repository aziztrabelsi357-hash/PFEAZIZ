
package com.example.PfaBack.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.PfaBack.models.Animal;
import com.example.PfaBack.models.Farm;
import com.example.PfaBack.models.Plant;
import com.example.PfaBack.repository.AnimalRepository;
import com.example.PfaBack.repository.FarmRepository;
import com.example.PfaBack.repository.PlantRepository;
import com.example.PfaBack.repository.UserRepository;

@Service
public class FarmService {

    private final FarmRepository farmRepository;
    private final AnimalRepository animalRepository;
    private final PlantRepository plantRepository;
    private final UserRepository userRepository;


@Autowired
public FarmService(FarmRepository farmRepository,
                   AnimalRepository animalRepository,
                   PlantRepository plantRepository,
                   UserRepository userRepository) {
    this.farmRepository = farmRepository;
    this.animalRepository = animalRepository;
    this.plantRepository = plantRepository;
    this.userRepository = userRepository;
}
      public Farm saveFarm(Farm farm) {
        return farmRepository.save(farm);
    }
public Farm createFarmForUser(String userId, Farm farm) {
    // Normalize / initialize lists
    if (farm.getAnimalIds() == null) farm.setAnimalIds(new java.util.ArrayList<>());
    if (farm.getPlantIds() == null) farm.setPlantIds(new java.util.ArrayList<>());
    farm.setUserId(userId);
    Farm saved = farmRepository.save(farm);

    userRepository.findById(userId).ifPresent(user -> {
        user.setFarm(saved);
        userRepository.save(user); // ensure persisted
    });

    return saved;
}

public void deleteFarm(String farmId) {
    farmRepository.deleteById(farmId);
}

public Farm getFarmById(String farmId) {
    return farmRepository.findById(farmId).orElse(null);
}

public Farm getFarmByUserId(String userId) {
    return farmRepository.findByUserId(userId).orElse(null);
}

public java.util.List<Animal> getAnimalsByFarm(String farmId) {
    return animalRepository.findByFarmId(farmId);
}

public Animal addAnimalToFarm(String farmId, Animal animal) {
    Farm farm = farmRepository.findById(farmId)
            .orElseThrow(() -> new RuntimeException("Farm not found: " + farmId));

    // Set foreign key
    animal.setFarmId(farmId);
    Animal savedAnimal = animalRepository.save(animal);

    // Update farm’s animalIds
    if (farm.getAnimalIds() == null) {
        farm.setAnimalIds(new java.util.ArrayList<>());
    }
    farm.getAnimalIds().add(savedAnimal.getId());
    System.out.println("Updated farm's animalIds: " + farm.getAnimalIds());
    farmRepository.save(farm);

    return savedAnimal;
}

public java.util.List<Plant> getPlantsByFarm(String farmId) {
    return plantRepository.findByFarmId(farmId);
}

public Plant addPlantToFarm(String farmId, Plant plant) {
    Farm farm = farmRepository.findById(farmId)
            .orElseThrow(() -> new RuntimeException("Farm not found: " + farmId));

    plant.setFarmId(farmId);
    Plant savedPlant = plantRepository.save(plant);

    if (farm.getPlantIds() == null) {
        farm.setPlantIds(new java.util.ArrayList<>());
    }
    farm.getPlantIds().add(savedPlant.getId());
    farmRepository.save(farm);

    return savedPlant; 
  } 
}