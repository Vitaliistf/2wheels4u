package org.vitaliistf.twowheels4u.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
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
import org.vitaliistf.twowheels4u.service.NotificationService;
import org.vitaliistf.twowheels4u.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final NotificationService notificationService;

    @PutMapping("/{id}/role")
    @Operation(summary = "Endpoint for updating the role of user by a manager.")
    public UserResponseDto updateRole(
            @Parameter(description = "User ID.")
            @PathVariable Long id,
            @Parameter(description = "Role (MANAGER, CUSTOMER).")
            @RequestParam String role) {
        User userById = userService.findById(id);
        userById.setRole(User.Role.valueOf(role));

        notificationService.sendMessageToAdmin(
                "User with id: " + id + " was updated with role: " + role);

        return userMapper.toDto(userService.update(userById));
    }

    @GetMapping("/me")
    @Operation(summary = "Endpoint to get current user information.")
    public UserResponseDto getCurrentUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userMapper.toDto(userService.findByEmail(userDetails.getUsername()));
    }

    @PutMapping("/me")
    @Operation(summary = "Endpoint to update current user information (except role).")
    public UserResponseDto updateInfo(
            Authentication authentication,
            @Parameter(schema = @Schema(implementation = UserRequestDto.class))
            @RequestBody UserRequestDto userRequestDto) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User userToUpdate = userService.findByEmail(userDetails.getUsername());
        User user = userMapper.toModel(userRequestDto);
        user.setId(userToUpdate.getId());
        user.setRole(userToUpdate.getRole());
        return userMapper.toDto(userService.update(user));
    }
}
