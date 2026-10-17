package com.pawhome.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/** Request and response objects used by the controllers. */
public class Dto {
    public record RegisterRequest(@NotBlank(message = "Name is required") String name,
                                  @NotBlank(message = "Email is required") @Email(message = "Enter a valid email") String email,
                                  @NotBlank(message = "Password is required") @Size(min = 6, message = "Password must be at least 6 characters") String password,
                                  String phone, String city) {}
    public record LoginRequest(@NotBlank(message = "Email is required") String email,
                               @NotBlank(message = "Password is required") String password) {}
    public record AuthResponse(String token, String name, String email, String role) {}
    public record CreateRequest(@NotNull(message = "Pet is required") Long petId,
                                @NotBlank(message = "Kind is required") String kind,
                                @NotBlank(message = "Please add a short note") @Size(max = 500) String note) {}
    public record RequestResponse(Long id, Long petId, String petName, String userName, String userEmail,
                                  String kind, String note, String status, LocalDateTime createdAt) {}
    public record Stats(long totalPets, long available, long placed, long pendingRequests) {}
}
