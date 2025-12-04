package com.edutrack.backend.service;

import com.edutrack.backend.dto.AuthRequest;
import com.edutrack.backend.dto.AuthResponse;
import com.edutrack.backend.dto.UserDto;
import com.edutrack.backend.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;


    public AuthResponse authenticate(AuthRequest request) {
        try {
            // Аутентификация через Spring Security
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            return AuthResponse.error("Invalid credentials");
        }

        // Если аутентификация успешна, генерируем токен
        User user = userService.getUserByEmail(request.getEmail());
        String token = jwtService.generateToken(user.getEmail());
        UserDto userDto = mapToUserDto(user);

        return AuthResponse.success(token, userDto);
    }

    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setSemester(user.getSemester());
        return userDto;
    }
}