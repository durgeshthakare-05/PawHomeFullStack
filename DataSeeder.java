package com.pawhome.config;

import com.pawhome.entity.Pet;
import com.pawhome.entity.User;
import com.pawhome.repository.PetRepository;
import com.pawhome.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/** Adds demo accounts and pets the first time the database is empty. */
@Component
public class DataSeeder implements CommandLineRunner {
    private final UserRepository users;
    private final PetRepository pets;
    private final PasswordEncoder encoder;

    public DataSeeder(UserRepository users, PetRepository pets, PasswordEncoder encoder) {
        this.users = users; this.pets = pets; this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (users.count() == 0) {
            users.save(user("Shelter Admin", "admin@pawhome.com", "admin123", "ADMIN"));
            users.save(user("Riya Sharma", "riya@pawhome.com", "riya123", "ADOPTER"));
        }
        if (pets.count() == 0) {
            pets.save(pet("Bruno", "Dog", "Labrador", 2, "Male", "Friendly and loves to play fetch."));
            pets.save(pet("Misty", "Cat", "Persian", 1, "Female", "Calm lap cat, likes quiet homes."));
            pets.save(pet("Coco", "Dog", "Beagle", 4, "Female", "Curious and good with children."));
            pets.save(pet("Luna", "Cat", "Indie", 3, "Female", "Independent and affectionate."));
            pets.save(pet("Thumper", "Rabbit", "Dutch", 1, "Male", "Gentle and easy to handle."));
        }
    }

    private User user(String name, String email, String pass, String role) {
        User u = new User();
        u.setName(name); u.setEmail(email); u.setPasswordHash(encoder.encode(pass)); u.setRole(role);
        return u;
    }

    private Pet pet(String name, String type, String breed, int age, String gender, String desc) {
        Pet p = new Pet();
        p.setName(name); p.setType(type); p.setBreed(breed); p.setAge(age); p.setGender(gender); p.setDescription(desc);
        return p;
    }
}
