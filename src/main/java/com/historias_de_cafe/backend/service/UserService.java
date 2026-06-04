package com.historias_de_cafe.backend.service;

import com.historias_de_cafe.backend.model.Role;
import com.historias_de_cafe.backend.model.User;
import com.historias_de_cafe.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    public User create(User user) {
        user.setId(null);
        user.setCreationDate(LocalDateTime.now());
        if (user.getStateActive() == null) {
            user.setStateActive(true);
        }
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User update(Long id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPasswordHash(user.getPasswordHash());
        existingUser.setRole(user.getRole());
        existingUser.setStateActive(user.getStateActive());

        return userRepository.save(existingUser);
    }

    public User patch(Long id, Map<String, Object> fields) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (fields.containsKey("name")) existingUser.setName((String) fields.get("name"));
        if (fields.containsKey("email")) existingUser.setEmail((String) fields.get("email"));
        if (fields.containsKey("passwordHash")) existingUser.setPasswordHash((String) fields.get("passwordHash"));
        if (fields.containsKey("stateActive")) existingUser.setStateActive((Boolean) fields.get("stateActive"));
        if (fields.containsKey("role")) existingUser.setRole(Role.valueOf((String) fields.get("role")));


        return userRepository.save(existingUser);
    }

    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
    }
}
