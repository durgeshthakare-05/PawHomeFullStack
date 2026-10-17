package com.pawhome.repository;

import com.pawhome.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    long countByStatus(String status);
    List<Pet> findAllByOrderByIdDesc();
}
