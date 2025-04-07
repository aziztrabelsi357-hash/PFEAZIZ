package com.example.PfaBack.controller;

import com.example.PfaBack.models.Article;
import com.example.PfaBack.models.CareTip;
import com.example.PfaBack.Services.CareTipService;
import com.example.PfaBack.repository.CareTipRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/care-tips")
public class CareTipController {

    @Autowired
    private CareTipService careTipService;
    @Autowired
    private CareTipRepository careTipRepository;

    @GetMapping
    public ResponseEntity<List<CareTip>> getAllCareTips() {
        List<CareTip> careTips = careTipService.getAllCareTips();
        return ResponseEntity.ok(careTips);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CareTip> getCareTipById(@PathVariable String id) {
        Optional<CareTip> careTip = careTipService.getCareTipById(id);
        return careTip.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(null));
    }

    @PostMapping
    public ResponseEntity<CareTip> createCareTip(@RequestBody CareTip careTip) {
        CareTip createdCareTip = careTipService.createCareTip(careTip);
        return ResponseEntity.ok(createdCareTip);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CareTip> updateCareTip(@PathVariable String id, @RequestBody CareTip updatedCareTip) {
        try {
            CareTip careTip = careTipService.updateCareTip(id, updatedCareTip);
            return ResponseEntity.ok(careTip);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCareTip(@PathVariable String id) {
        careTipService.deleteCareTip(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/init")
    public ResponseEntity<String> initializeCareTips() {
        List<CareTip> careTips = List.of(
                new CareTip(
                        null,
                        "Nutritional Needs of Puppies",
                        "Essential guide to feeding your puppy for optimal growth",
                        new Article(
                                "What Are the Nutritional Needs of My Puppy and Young Dog?",
                                "Feeding Your Puppy\nA young puppy's energy needs are proportionally higher than those of an adult dog.\n\nYou shouldn't feed your puppy a standard adult dog food; they need highly nutritious, specialized food to give them the best possible start in life and support their healthy development.\n\nPuppies are still fragile and need support to meet three essential requirements: growing harmoniously, adapting to digesting something other than maternal milk, and developing a competent immune system."
                        )
                ),
                new CareTip(
                        null,
                        "Dog Skin Care Guide",
                        "How to properly care for your dog's skin and coat",
                        new Article(
                                "How to Take Care of Your Dog's Skin?",
                                "Maintaining the Coat\nTaking care of your dog's skin, eyes, and ears is essential for their health and well-being.\n\nA dog's skin and coat condition often reflect its overall health.\n\nThe first step in regular maintenance is frequent brushing. This provides an opportunity to examine the skin closely and remove dead hair and impurities, promoting healthy hair regrowth. Brushing should be increased during the spring and fall shedding seasons."
                        )
                ),
                new CareTip(
                        null,
                        "Male Dog Sterilization",
                        "Everything you need to know about sterilizing your male dog",
                        new Article(
                                "Male Dog Sterilization",
                                "Why Sterilize Your Male Dog?\nMedical Reasons\nUnneutered male dogs are at risk of developing reproductive system diseases as they age, including testicular tumors and prostate disorders. Sterilization reduces or helps manage these health risks.\n\nPreventing Unwanted Breeding\nSterilization may be necessary to prevent unwanted breeding, especially when a male and female dog live in the same household."
                        )
                ),
                new CareTip(
                        null,
                        "Toxic Foods for Dogs",
                        "Common human foods that are dangerous for dogs",
                        new Article(
                                "Foods to Avoid for Dogs",
                                "Many common human foods can be toxic to dogs.\n\nChocolate\nChocolate contains theobromine, which stimulates the central nervous system. Dogs metabolize this substance much more slowly than humans, making even small amounts dangerous. For a dog weighing 5 to 10 kg, consuming 40–80 g of dark chocolate can be harmful."
                        )
                ),
                new CareTip(
                        null,
                        "Managing Dog Stress",
                        "How to recognize and handle stress in your dog",
                        new Article(
                                "How to Handle a Stressed Dog?",
                                "Dogs can experience stress, and managing it involves preventing anxiety-inducing situations.\n\nStressful situations can be temporary (car rides, grooming, vacations) or prolonged (separation anxiety, moving, introduction of a new pet or baby).\n\nCertain noises (fireworks, thunderstorms) or past traumas (accidents, negative experiences) can trigger stress. Even visits to the veterinarian can be highly stressful for some dogs."
                        )
                )
        );

        careTipRepository.saveAll(careTips);

        return ResponseEntity.ok("Care tips successfully initialized and inserted into MongoDB.");
    }
}