package org.vitaliistf.twowheels4u.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vitaliistf.twowheels4u.exception.UserAlreadyRegisteredException;
import org.vitaliistf.twowheels4u.model.User;
import org.vitaliistf.twowheels4u.repository.UserRepository;
import org.vitaliistf.twowheels4u.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User save(User user) {
        if (userExistsByEmail(user.getEmail())) {
            throw new UserAlreadyRegisteredException("User with such email is already registered.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        User userFromDb = userRepository.findById(user.getId()).orElseThrow(() ->
                new NoSuchElementException("Cannot find user with id: " + user.getId()));
        userFromDb.setEmail(user.getEmail());
        userFromDb.setPassword(passwordEncoder.encode(user.getPassword()));
        userFromDb.setFirstName(user.getFirstName());
        userFromDb.setLastName(user.getLastName());
        userFromDb.setRole(user.getRole());
        userFromDb.setTelegramId(user.getTelegramId());
        return userRepository.save(userFromDb);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Cannot find user with id: " + id));
    }

    @Override
    @Transactional
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new NoSuchElementException("Cannot find user with email: " + email));
    }

    private boolean userExistsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public List<User> findByRoles(User.Role role) {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == role)
                .collect(Collectors.toList());
    }
}
