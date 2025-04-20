package com.business_card.business_card.controller;

import com.business_card.business_card.model.BusinessProfile;
import com.business_card.business_card.service.BusinessProfileService;
import com.business_card.business_card.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@CrossOrigin(origins = { "http://buisness-card-app.s3-website.ap-south-1.amazonaws.com","http://localhost:4200"})
public class BusinessProfileController {

    @Autowired
    private BusinessProfileService profileService;

    @Autowired
    private JwtUtil jwtUtil;

    private String extractPhone(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return jwtUtil.extractPhone(token);
    }

    @PostMapping
    public ResponseEntity<?> createProfile(@RequestBody BusinessProfile profile, HttpServletRequest request) {
        String phone = extractPhone(request);
        BusinessProfile saved = profileService.createProfile(profile, phone);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<?> getUserProfiles(HttpServletRequest request) {
        String phone = extractPhone(request);
        List<BusinessProfile> profiles = profileService.getProfiles(phone);
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfileById(@PathVariable Long id, HttpServletRequest request) {
        String phone = extractPhone(request);
        BusinessProfile profile = profileService.getProfileById(id, phone);
        return ResponseEntity.ok(profile);
    }

}
