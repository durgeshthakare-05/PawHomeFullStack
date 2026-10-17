package com.pawhome.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
public class AdoptionRequest {
    public static final String PENDING = "PENDING";
    public static final String APPROVED = "APPROVED";
    public static final String REJECTED = "REJECTED";

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "pet_id")
    private Pet pet;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(nullable = false)
    private String kind; // ADOPT or FOSTER
    @Column(length = 500)
    private String note;
    @Column(nullable = false)
    private String status = PENDING;
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public Pet getPet() { return pet; }
    public void setPet(Pet pet) { this.pet = pet; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getKind() { return kind; }
    public void setKind(String kind) { this.kind = kind; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
