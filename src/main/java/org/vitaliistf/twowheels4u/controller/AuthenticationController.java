package org.vitaliistf.twowheels4u.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.vitaliistf.twowheels4u.dto.request.UserLoginRequestDto;
import org.vitaliistf.twowheels4u.dto.request.UserRegisterRequestDto;
import org.vitaliistf.twowheels4u.dto.response.UserResponseDto;
import org.vitaliistf.twowheels4u.mapper.UserMapper;
import org.vitaliistf.twowheels4u.model.User;
import org.vitaliistf.twowheels4u.security.AuthenticationService;
import org.vitaliistf.twowheels4u.security.jwt.JwtService;
import org.vitaliistf.twowheels4u.service.NotificationService;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final NotificationService notificationService;

    @PostMapping("/register")
    @Operation(summary = "Endpoint for registration.",
            description = "Provide data for registration. Make sure that password and "
                    + "repeatPassword are the same, have at least 8 symbols length and have at "
                    + "least one number, lowercase and uppercase letter.")
    public UserResponseDto register(
            @Parameter(schema = @Schema(implementation = UserRegisterRequestDto.class))
            @RequestBody @Valid UserRegisterRequestDto userRegisterDto) {
        User user = authenticationService.register(userRegisterDto.getEmail(),
                userRegisterDto.getPassword(),
                userRegisterDto.getFirstName(),
                userRegisterDto.getLastName());

        notificationService.sendMessageToAdmin(
                "New user was registered with email: " + user.getEmail()
        );

        return userMapper.toDto(user);
    }

    @PostMapping("/login")
    @Operation(summary = "Endpoint for login into account.",
            description = "Provide email and password for authentication.")
    public ResponseEntity<Object> login(
            @Parameter(schema = @Schema(implementation = UserLoginRequestDto.class))
            @RequestBody @Valid UserLoginRequestDto userLoginDto) {
        User user = authenticationService.login(userLoginDto.getEmail(),
                userLoginDto.getPassword());
        String token = jwtService.createToken(user.getEmail(), user.getRole());
        return new ResponseEntity<>(Map.of("token", token), HttpStatus.OK);
    }
}
