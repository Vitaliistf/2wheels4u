package org.vitaliistf.twowheels4u.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.vitaliistf.twowheels4u.dto.request.UserRequestDto;
import org.vitaliistf.twowheels4u.dto.response.UserResponseDto;
import org.vitaliistf.twowheels4u.mapper.UserMapper;
import org.vitaliistf.twowheels4u.model.User;
import org.vitaliistf.twowheels4u.service.UserService;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PutMapping("/{id}/role")
    public UserResponseDto updateRole(@PathVariable Long id, @RequestParam String role) {
        User userById = userService.findById(id);
        userById.setRole(User.Role.valueOf(role));

        return userMapper.toDto(userService.update(userById));
    }

    @GetMapping("/me")
    public UserResponseDto getCurrentUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userMapper.toDto(userService.findByEmail(userDetails.getUsername()));
    }

    @PutMapping("/me")
    public UserResponseDto updateInfo(Authentication authentication,
                                      @RequestBody UserRequestDto userRequestDto) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User userToUpdate = userService.findByEmail(userDetails.getUsername());
        User user = userMapper.toModel(userRequestDto);
        user.setId(userToUpdate.getId());
        user.setRole(userToUpdate.getRole());
        return userMapper.toDto(userService.update(user));
    }
}
