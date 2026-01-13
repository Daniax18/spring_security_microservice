package com.daniax.auth_service.controller;

import com.daniax.auth_service.dto.AuthResponse;
import com.daniax.auth_service.dto.LoginUserDto;
import com.daniax.auth_service.dto.RegisterUserDto;
import com.daniax.auth_service.entity.User;
import com.daniax.auth_service.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserDto userDto) {
        try {
            AuthResponse response = authService.login(userDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(
                    ex.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUserDto userDto) {
        try {
            User user = new User(userDto.getUserName(), userDto.getEmail(), userDto.getMdp(), userDto.getRole());
            return new ResponseEntity<>(authService.create(user), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(
                    ex.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
