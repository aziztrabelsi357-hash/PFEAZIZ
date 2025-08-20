package com.example.PfaBack.controller;

import java.util.List;
import java.util.Map;

import com.example.PfaBack.repository.FarmRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.PfaBack.Services.AnimalService;
import com.example.PfaBack.Services.FarmService;
import com.example.PfaBack.Services.PlantService;
import com.example.PfaBack.models.Animal;
import com.example.PfaBack.models.Farm;
import com.example.PfaBack.models.Plant;
import com.example.PfaBack.models.WaterTank;

@RestController
@RequestMapping("/api/farms")
public class FarmController {

    @Autowired
    private FarmService farmService;
    @Autowired
    private PlantService plantService;
    @Autowired
    private AnimalService animalService;  


    FarmController(FarmService farmService, PlantService plantService, AnimalService animalService) {
        this.farmService = farmService;
        this.plantService = plantService;
        this.animalService = animalService;

    }

    // ✅ Create farm for a user
    @PostMapping("/user/{userId}")
    public Farm createFarmForUser(@PathVariable String userId, @RequestBody Farm farm) {
        System.out.println("Creating farm for user: " + userId);
        System.err.println("Farm details: " + farm);
        return farmService.createFarmForUser(userId, farm);
    }

    // ✅ Delete farm
    @DeleteMapping("/{farmId}")
    public void deleteFarm(@PathVariable String farmId) {
        farmService.deleteFarm(farmId);
    }

    // ✅ Get farm details
    @GetMapping("/{farmId}")
    public Farm getFarmById(@PathVariable String farmId) {
        Farm farm = farmService.getFarmById(farmId);
        if (farm == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.NOT_FOUND, "Farm not found: " + farmId);
        }
        return farm;
    }

    // ✅ Get farm by user id
    @GetMapping("/user/{userId}")
    public Farm getFarmByUserId(@PathVariable String userId) {
        return farmService.getFarmByUserId(userId);
    }

    // ✅ Get animals of a farm
    @GetMapping("/{farmId}/animals")
    public List<Animal> getAnimalsByFarm(@PathVariable String farmId) {
        Farm farm = farmService.getFarmById(farmId);
        if (farm == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.NOT_FOUND, "Farm not found: " + farmId);
        }
        return farmService.getAnimalsByFarm(farmId);
    }

    // ✅ Add animal to farm
    @PostMapping("/{farmId}/animals")
    public Animal addAnimalToFarm(@PathVariable String farmId, @RequestBody Animal animal) {
        return farmService.addAnimalToFarm(farmId, animal);
    }

    // ✅ Get plants of a farm
    @GetMapping("/{farmId}/plants")
    public List<Plant> getPlantsByFarm(@PathVariable String farmId) {
        return farmService.getPlantsByFarm(farmId);
    }

    // ✅ Add plant to farm
    @PostMapping("/{farmId}/plants")
    public Plant addPlantToFarm(@PathVariable String farmId, @RequestBody Plant plant) {
        return farmService.addPlantToFarm(farmId, plant);
    }

    // Watering dashboard endpoints (placeholder logic)
    // --- WaterTank endpoints ---
    @GetMapping("/{farmId}/tank-level")
    public java.util.Map<String,Object> getTankLevel(@PathVariable String farmId) {
        Farm farm = farmService.getFarmById(farmId);
        if (farm == null || farm.getWaterTank() == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.NOT_FOUND, "Farm or tank not found: " + farmId);
        }
        double level = farm.getWaterTank().getQuantity();
        boolean low = level < 200; // threshold
        java.util.Map<String,Object> map = new java.util.HashMap<>();
        map.put("level", level);
        map.put("low", low);
        return map;
    }

    @GetMapping("/{farmId}/water-usage")
    public java.util.Map<String,Double> getWaterUsage(@PathVariable String farmId) {
        Farm farm = farmService.getFarmById(farmId);
        if (farm == null || farm.getWaterTank() == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.NOT_FOUND, "Farm or tank not found: " + farmId);
        }
        // Sum all usage
        double total = 0.0;
        if (farm.getWaterTank().getUsageByDate() != null) {
            for (double v : farm.getWaterTank().getUsageByDate().values()) total += v;
        }
        return java.util.Map.of("total", total);
    }

    @PostMapping("/{farmId}/tank/refill")
    public java.util.Map<String,Object> refillTank(@PathVariable String farmId, @RequestParam double amount) {
        Farm farm = farmService.getFarmById(farmId);
        if (farm == null || farm.getWaterTank() == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.NOT_FOUND, "Farm or tank not found: " + farmId);
        }
        double newQ = farm.getWaterTank().getQuantity() + amount;
        farm.getWaterTank().setQuantity(newQ);
        farmService.saveFarm(farm);
        return java.util.Map.of("level", newQ);
    }

    @GetMapping("/{farmId}/tank/usage-by-date")
    public java.util.Map<String,Double> getUsageByDate(@PathVariable String farmId, @RequestParam String date) {
        Farm farm = farmService.getFarmById(farmId);
        if (farm == null || farm.getWaterTank() == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.NOT_FOUND, "Farm or tank not found: " + farmId);
        }
        Double used = 0.0;
        if (farm.getWaterTank().getUsageByDate() != null) {
            used = farm.getWaterTank().getUsageByDate().getOrDefault(date, 0.0);
        }
        return java.util.Map.of("used", used);
    }

    @GetMapping("/{farmId}/ai-suggestion")
    public java.util.Map<String,Object> getAiSuggestion(@PathVariable String farmId) {
        return plantService.getAiSuggestion(farmId);
    }

    @GetMapping("/{farmId}/rain-forecast")
    public java.util.Map<String,Boolean> getRainForecast(@PathVariable String farmId) {
        return java.util.Map.of("rain", plantService.getRainForecast(farmId));
    }

    @GetMapping("/{farmId}/today-intake")
    public java.util.Map<String,Integer> getTodayIntake(@PathVariable String farmId) {
        return java.util.Map.of("intake", plantService.getTodayIntake(farmId));
    }

    @GetMapping("/{farmId}/schedule")
    public java.util.Map<String,Object> getSchedule(@PathVariable String farmId) {
        return plantService.getSchedule(farmId);
    }

  @PostMapping("/{farmId}/plants/{plantId}/water")
      public Plant waterPlantAndDecreaseTank(
          @PathVariable String farmId,
          @PathVariable String plantId,
          @RequestParam int liters,
          @RequestBody(required = false) Map<String, Object> body // Accepts JSON body
      ) {
          Integer soilMoisture = null;
      if (body != null && body.containsKey("soilMoisture")) {
          Object val = body.get("soilMoisture");
          if (val instanceof Number) {
              soilMoisture = ((Number) val).intValue();
          }
      }
        
          return plantService.waterPlant(plantId, liters, soilMoisture);
      }

    // ✅ Get animals of a farm with treatment or vaccination today
    @GetMapping("/{farmId}/animals/today-schedule")
    public List<Animal> getAnimalsWithTodaySchedule(@PathVariable String farmId) {
        List<Animal> animals = farmService.getAnimalsByFarm(farmId);
        java.time.LocalDate today = java.time.LocalDate.now();
        List<Animal> result = new java.util.ArrayList<>();
        for (Animal animal : animals) {
            boolean match = false;
            if (animal.getNextVisit() != null && !animal.getNextVisit().isEmpty()) {
                try {
                    java.time.LocalDate visitDate = java.time.LocalDate.parse(animal.getNextVisit());
                    if (visitDate.equals(today)) match = true;
                } catch (Exception ignored) {}
            }
            if (animal.getVaccinationDate() != null && !animal.getVaccinationDate().isEmpty()) {
                try {
                    java.time.LocalDate vaccDate = java.time.LocalDate.parse(animal.getVaccinationDate());
                    if (vaccDate.equals(today)) match = true;
                } catch (Exception ignored) {}
            }
            if (match) result.add(animal);
        }
        return result;
    }

    // ✅ Get plants of a farm with treatment or harvest today
    @GetMapping("/{farmId}/plants/today-schedule")
    public List<Plant> getPlantsWithTodaySchedule(@PathVariable String farmId) {
        List<Plant> plants = farmService.getPlantsByFarm(farmId);
        java.time.LocalDate today = java.time.LocalDate.now();
        List<Plant> result = new java.util.ArrayList<>();
        for (Plant plant : plants) {
            boolean match = false;
            if (plant.getNextTreatment() != null && !plant.getNextTreatment().isEmpty()) {
                try {
                    java.time.LocalDate treatDate = java.time.LocalDate.parse(plant.getNextTreatment());
                    if (treatDate.equals(today)) match = true;
                } catch (Exception ignored) {}
            }
            if (plant.getExpectedHarvestDate() != null && !plant.getExpectedHarvestDate().isEmpty()) {
                try {
                    java.time.LocalDate harvestDate = java.time.LocalDate.parse(plant.getExpectedHarvestDate());
                    if (harvestDate.equals(today)) match = true;
                } catch (Exception ignored) {}
            }
            if (match) result.add(plant);
        }
        return result;
    }

    
    // Endpoint to feed an animal (add to todayIntakeLiters)
    @PostMapping("/{farmId}/animals/{animalId}/feed")
    public Animal feedAnimal(@PathVariable String farmId, @PathVariable String animalId, @RequestParam double liters) {
        // Optionally: check farmId matches animal's farmId
        return animalService.addIntake(animalId, liters);
    }
}
