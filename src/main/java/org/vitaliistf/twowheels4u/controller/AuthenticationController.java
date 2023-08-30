package org.vitaliistf.twowheels4u.controller;

import jakarta.validation.Valid;
import java.util.Map;
import lombok.AllArgsConstructor;
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

@RestController
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public UserResponseDto register(@RequestBody @Valid UserRegisterRequestDto userRegisterDto) {
        User user = authenticationService.register(userRegisterDto.getEmail(),
                userRegisterDto.getPassword(),
                userRegisterDto.getFirstName(),
                userRegisterDto.getLastName());
        return userMapper.toDto(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid UserLoginRequestDto userLoginDto) {
        User user = authenticationService.login(userLoginDto.getEmail(),
                userLoginDto.getPassword());
        String token = jwtService.createToken(user.getEmail(), user.getRole());
        return new ResponseEntity<>(Map.of("token", token), HttpStatus.OK);
    }
}
