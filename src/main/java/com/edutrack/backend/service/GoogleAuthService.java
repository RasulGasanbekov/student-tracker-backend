package com.edutrack.backend.service;

import com.edutrack.backend.dto.AuthResponse;
import com.edutrack.backend.dto.UserDto;
import com.edutrack.backend.entity.User;
import com.edutrack.backend.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Value("${google.client-id}")
    private String googleClientId;


    public AuthResponse authenticateWithGoogle(String googleToken) {
        try {
            // Верификация токена Google
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance()
            )
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(googleToken);
            if (idToken == null) {
                return AuthResponse.error("Invalid Google token");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();

            // Ищем пользователя в нашей базе по email
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found in system"));

            // Генерируем наш JWT токен
            String ourToken = jwtService.generateToken(user.getEmail());
            UserDto userDto = mapToUserDto(user);

            return AuthResponse.success(ourToken, userDto);

        } catch (GeneralSecurityException | IOException e) {
            return AuthResponse.error("Google token verification failed");
        } catch (RuntimeException e) {
            return AuthResponse.error("User not found in system");
        }
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