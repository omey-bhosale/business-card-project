package com.business_card.business_card.controller;

import com.business_card.business_card.dto.EmailLoginRequest;
import com.business_card.business_card.dto.OtpLoginRequest;
import com.business_card.business_card.model.User;
import com.business_card.business_card.repository.UserRepository;
import com.business_card.business_card.service.UserService;
import com.business_card.business_card.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200") // Angular frontend URL
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam String phone) {
        return userService.sendOtp(phone);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String phone, @RequestParam String otp) {
        boolean verified = userService.verifyOtp(phone, otp);
        return ResponseEntity.ok(Map.of("verified", verified));
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        User savedUser = userService.registerUser(user);
        String token = jwtUtil.generateToken(savedUser.getPhone());
        return ResponseEntity.ok(Map.of(
                "token", token,
                "message", "User registered successfully",
                "user", savedUser
        ));
    }

    @GetMapping("/protected")
    public String protectedEndpoint() {
        return "You're authenticated!";
    }

    // import dto classes and jwtUtil, userService...

    @PostMapping("/login-email")
    public ResponseEntity<?> loginWithEmail(@RequestBody EmailLoginRequest request) {
        return userService.loginWithEmail(request.getEmail(), request.getPassword())
                .map(user -> ResponseEntity.ok(Map.of(
                        "token", jwtUtil.generateToken(user.getPhone()),
                        "message", "Login successful",
                        "user", user)))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                        "message", "Invalid email or password"
                )));
    }

    public Optional<User> loginWithOtp(String phone, String otp) {
        return userRepository.findByPhone(phone)
                .filter(user -> otp.equals(user.getOtp()))
                .map(user -> {
                    user.setOtp(null); // clear OTP after login
                    userRepository.save(user);
                    return user;
                });
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String uploadDir = "uploads/";
        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) uploadPath.mkdirs();

        String filePath = uploadDir + UUID.randomUUID() + "_" + file.getOriginalFilename();
        File dest = new File(filePath);
        file.transferTo(dest);

        String url = "http://localhost:8080/" + filePath; // or serve from static location
        return ResponseEntity.ok(Map.of("url", url));
    }




}
