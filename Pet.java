package com.pawhome.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "pets")
public class Pet {
    public static final String AVAILABLE = "AVAILABLE";
    public static final String FOSTERED = "FOSTERED";
    public static final String ADOPTED = "ADOPTED";

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Type is required")
    private String type; // Dog, Cat, Rabbit ...
    @NotBlank(message = "Breed is required")
    private String breed;
    @Min(value = 0, message = "Age cannot be negative")
    private int age;
    private String gender;
    @Column(length = 500)
    private String description;
    @Column(nullable = false)
    private String status = AVAILABLE;
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
