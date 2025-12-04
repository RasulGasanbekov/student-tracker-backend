package com.edutrack.backend.controller;


import com.edutrack.backend.dto.ProgressDto;
import com.edutrack.backend.entity.User;
import com.edutrack.backend.service.ProgressService;
import com.edutrack.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class ProgressController {
    private final ProgressService progressService;
    private final UserService userService;


    @GetMapping
    public ResponseEntity<ProgressDto> getProgress(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        ProgressDto progress = progressService.getProgressByUserId(user.getId());
        return ResponseEntity.ok(progress);
    }
}