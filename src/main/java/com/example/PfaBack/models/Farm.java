package com.example.PfaBack.models;

import java.util.List;
import com.example.PfaBack.models.WaterTank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "farms")
public class Farm {
    @Id
    private String id;
    private String name;
    private String location;
    private List<String> animalIds; // store animal IDs
    private List<String> plantIds;
    private String userId; // store user ID
    private WaterTank waterTank;


    // Constructors, getters, and setters
    public Farm() {
        this.waterTank = new WaterTank();
    }

    public Farm(String name, String location, List<String> animalIds, List<String> plantIds, String userId) {
        this.name = name;
        this.location = location;
        this.animalIds = animalIds;
        this.plantIds = plantIds;
        this.userId = userId;
        this.waterTank = new WaterTank();
    }
    public WaterTank getWaterTank() {
        return waterTank;
    }

    public void setWaterTank(WaterTank waterTank) {
        this.waterTank = waterTank;
    }

  public String getId() {
      return id;
  }

  public void setId(String id) {
      this.id = id;
  }

  public String getName() {
      return name;
  }

  public void setName(String name) {
      this.name = name;
  }

  public String getLocation() {
      return location;
  }

  public void setLocation(String location) {
      this.location = location;
  }

  public List<String> getAnimalIds() {
      return animalIds;
  }

  public void setAnimalIds(List<String> animalIds) {
      this.animalIds = animalIds;
  }

  public List<String> getPlantIds() {
      return plantIds;
  }

  public void setPlantIds(List<String> plantIds) {
      this.plantIds = plantIds;
  }

  public String getUserId() {
      return userId;
  }

  public void setUserId(String userId) {
      this.userId = userId;
  }
}
