package com.pawhome.controller;

import com.pawhome.controller.Dto.Stats;
import com.pawhome.entity.AdoptionRequest;
import com.pawhome.entity.Pet;
import com.pawhome.repository.PetRepository;
import com.pawhome.repository.RequestRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final PetRepository pets;
    private final RequestRepository requests;

    public AdminController(PetRepository pets, RequestRepository requests) {
        this.pets = pets; this.requests = requests;
    }

    @GetMapping("/stats")
    public Stats stats() {
        long placed = pets.countByStatus(Pet.ADOPTED) + pets.countByStatus(Pet.FOSTERED);
        return new Stats(pets.count(), pets.countByStatus(Pet.AVAILABLE), placed,
                requests.countByStatus(AdoptionRequest.PENDING));
    }
}
