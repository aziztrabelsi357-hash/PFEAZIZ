package com.example.PfaBack.controller;

import com.example.PfaBack.models.CareTip;
import com.example.PfaBack.Services.CareTipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/care-tips")
public class AdminCareTipController {

    @Autowired
    private CareTipService careTipService;

    @GetMapping
    public ResponseEntity<List<CareTip>> getAllCareTips() {
        return ResponseEntity.ok(careTipService.getAllCareTips());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CareTip> getCareTipById(@PathVariable String id) {
        Optional<CareTip> careTip = careTipService.getCareTipById(id);
        return careTip.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).build());
    }

    @PostMapping
    public ResponseEntity<CareTip> createCareTip(@RequestBody CareTip careTip) {
        return ResponseEntity.ok(careTipService.createCareTip(careTip));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CareTip> updateCareTip(@PathVariable String id, @RequestBody CareTip careTip) {
        try {
            CareTip updatedCareTip = careTipService.updateCareTip(id, careTip);
            return ResponseEntity.ok(updatedCareTip);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCareTip(@PathVariable String id) {
        careTipService.deleteCareTip(id);
        return ResponseEntity.ok().build();
    }
}