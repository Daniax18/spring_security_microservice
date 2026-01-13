package com.daniax.auth_service.service;

import com.daniax.auth_service.configuration.JwtUtils;
import com.daniax.auth_service.dto.AuthResponse;
import com.daniax.auth_service.dto.LoginUserDto;
import com.daniax.auth_service.entity.User;
import com.daniax.auth_service.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public User create(User user) throws Exception{
        if(userRepository.findByUserName(user.getUserName()) != null) throw new Exception("User already exists");

        user.setMdp(passwordEncoder.encode(user.getMdp()));
        return userRepository.save(user);
    }

    public AuthResponse login(LoginUserDto loginUserDto) throws Exception{
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserDto.getUserName(), loginUserDto.getMdp()));
        if(authentication.isAuthenticated()){
            User user = userRepository.findByUserName(loginUserDto.getUserName());

            // Générer le token
            String token = jwtUtils.generateToken(user.getUserName(), user.getRole().name());

            return new AuthResponse(
                    token,
                    user.getUserName(),
                    user.getRole().name()
            );
        }
        throw new Exception("Invalid mdp or user");
    }
}
