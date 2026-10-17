package com.pawhome.repository;

import com.pawhome.entity.AdoptionRequest;
import com.pawhome.entity.Pet;
import com.pawhome.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RequestRepository extends JpaRepository<AdoptionRequest, Long> {
    List<AdoptionRequest> findByUserOrderByIdDesc(User user);
    List<AdoptionRequest> findAllByOrderByIdDesc();
    List<AdoptionRequest> findByPetAndStatus(Pet pet, String status);
    boolean existsByPetAndUserAndStatus(Pet pet, User user, String status);
    long countByStatus(String status);
    void deleteByPet(Pet pet);
}
