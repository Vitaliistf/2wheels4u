package org.vitaliistf.twowheels4u.security.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.vitaliistf.twowheels4u.exception.AuthenticationException;
import org.vitaliistf.twowheels4u.model.User;
import org.vitaliistf.twowheels4u.security.AuthenticationService;
import org.vitaliistf.twowheels4u.service.UserService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User register(String email, String password, String firstName, String lastName) {
        User userToSave = new User();
        userToSave.setEmail(email);
        userToSave.setPassword(password);
        userToSave.setFirstName(firstName);
        userToSave.setLastName(lastName);
        userToSave.setRole(User.Role.CUSTOMER);
        return userService.save(userToSave);
    }

    @Override
    public User login(String email, String password) {
        User user = userService.findByEmail(email);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationException("Incorrect email or password.");
        }
        return user;
    }
}
