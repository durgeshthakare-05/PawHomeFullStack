package com.pawhome.service;

import com.pawhome.controller.Dto.CreateRequest;
import com.pawhome.controller.Dto.RequestResponse;
import com.pawhome.entity.AdoptionRequest;
import com.pawhome.entity.Pet;
import com.pawhome.entity.User;
import com.pawhome.repository.PetRepository;
import com.pawhome.repository.RequestRepository;
import com.pawhome.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class RequestService {
    private final RequestRepository requests;
    private final PetRepository pets;
    private final UserRepository users;

    public RequestService(RequestRepository requests, PetRepository pets, UserRepository users) {
        this.requests = requests; this.pets = pets; this.users = users;
    }

    public static RequestResponse toDto(AdoptionRequest r) {
        return new RequestResponse(r.getId(), r.getPet().getId(), r.getPet().getName(), r.getUser().getName(),
                r.getUser().getEmail(), r.getKind(), r.getNote(), r.getStatus(), r.getCreatedAt());
    }

    @Transactional
    public RequestResponse create(String email, CreateRequest dto) {
        User user = users.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
        Pet pet = pets.findById(dto.petId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found"));
        String kind = dto.kind().toUpperCase();
        if (!kind.equals("ADOPT") && !kind.equals("FOSTER"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kind must be ADOPT or FOSTER");
        if (!Pet.AVAILABLE.equals(pet.getStatus()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, pet.getName() + " is not available");
        if (requests.existsByPetAndUserAndStatus(pet, user, AdoptionRequest.PENDING))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You already have a pending request for this pet");
        AdoptionRequest r = new AdoptionRequest();
        r.setPet(pet); r.setUser(user); r.setKind(kind); r.setNote(dto.note().trim());
        return toDto(requests.save(r));
    }

    public List<RequestResponse> mine(String email) {
        User user = users.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
        return requests.findByUserOrderByIdDesc(user).stream().map(RequestService::toDto).toList();
    }

    public List<RequestResponse> all() {
        return requests.findAllByOrderByIdDesc().stream().map(RequestService::toDto).toList();
    }

    @Transactional
    public RequestResponse approve(Long id) {
        AdoptionRequest r = find(id);
        if (!AdoptionRequest.PENDING.equals(r.getStatus()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Request is already " + r.getStatus());
        Pet pet = r.getPet();
        if (!Pet.AVAILABLE.equals(pet.getStatus()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, pet.getName() + " is no longer available");
        r.setStatus(AdoptionRequest.APPROVED);
        pet.setStatus(r.getKind().equals("ADOPT") ? Pet.ADOPTED : Pet.FOSTERED);
        pets.save(pet);
        for (AdoptionRequest other : requests.findByPetAndStatus(pet, AdoptionRequest.PENDING)) {
            if (!other.getId().equals(r.getId())) {
                other.setStatus(AdoptionRequest.REJECTED);
                requests.save(other);
            }
        }
        return toDto(requests.save(r));
    }

    @Transactional
    public RequestResponse reject(Long id) {
        AdoptionRequest r = find(id);
        if (!AdoptionRequest.PENDING.equals(r.getStatus()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Request is already " + r.getStatus());
        r.setStatus(AdoptionRequest.REJECTED);
        return toDto(requests.save(r));
    }

    private AdoptionRequest find(Long id) {
        return requests.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));
    }
}
