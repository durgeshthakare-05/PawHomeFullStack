package com.pawhome.controller;

import com.pawhome.entity.Pet;
import com.pawhome.repository.PetRepository;
import com.pawhome.repository.RequestRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetController {
    private final PetRepository pets;
    private final RequestRepository requests;

    public PetController(PetRepository pets, RequestRepository requests) {
        this.pets = pets; this.requests = requests;
    }

    @GetMapping
    public List<Pet> list(@RequestParam(required = false) String q,
                          @RequestParam(required = false) String type,
                          @RequestParam(required = false) String status) {
        String needle = q == null ? "" : q.trim().toLowerCase();
        return pets.findAllByOrderByIdDesc().stream()
                .filter(p -> needle.isEmpty() || (p.getName() + " " + p.getBreed()).toLowerCase().contains(needle))
                .filter(p -> type == null || type.isBlank() || p.getType().equalsIgnoreCase(type))
                .filter(p -> status == null || status.isBlank() || p.getStatus().equalsIgnoreCase(status))
                .toList();
    }

    @GetMapping("/{id}")
    public Pet one(@PathVariable Long id) { return find(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pet add(@Valid @RequestBody Pet pet) {
        pet.setStatus(Pet.AVAILABLE);
        return pets.save(pet);
    }

    @PutMapping("/{id}")
    public Pet update(@PathVariable Long id, @Valid @RequestBody Pet in) {
        Pet p = find(id);
        p.setName(in.getName()); p.setType(in.getType()); p.setBreed(in.getBreed());
        p.setAge(in.getAge()); p.setGender(in.getGender()); p.setDescription(in.getDescription());
        return pets.save(p);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void delete(@PathVariable Long id) {
        Pet p = find(id);
        requests.deleteByPet(p);
        pets.delete(p);
    }

    private Pet find(Long id) {
        return pets.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found"));
    }
}
