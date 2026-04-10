package com.hupt.hupt_backend.services;

import com.hupt.hupt_backend.entities.User;
import com.hupt.hupt_backend.entities.UserType;
import com.hupt.hupt_backend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        return userRepository.save(user);
    }

    /**
     * Toggle the isActive flag of a user (used for Registrar desks).
     * When set to false, no new registrations will be routed to this desk.
     * Existing registrations are not affected.
     */
    public User setActive(Long userId, boolean active) {
        User user = getUserById(userId);
        user.setIsActive(active);
        return userRepository.save(user);
    }

    /**
     * Get all users with the Registrar role.
     */
    public List<User> getAllRegistrars() {
        return userRepository.findByRole(UserType.Registrar);
    }
}