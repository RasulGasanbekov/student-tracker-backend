package com.edutrack.backend.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private Boolean success;
    private String token;
    private UserDto user;
    private String error;

    public static AuthResponse success(String token, UserDto user) {
        AuthResponse response = new AuthResponse();
        response.setSuccess(true);
        response.setToken(token);
        response.setUser(user);
        return response;
    }

    public static AuthResponse error(String error) {
        AuthResponse response = new AuthResponse();
        response.setSuccess(false);
        response.setError(error);
        return response;
    }
}
