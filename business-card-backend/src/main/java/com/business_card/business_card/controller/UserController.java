package com.business_card.business_card.controller;

import com.business_card.business_card.dto.EmailLoginRequest;
import com.business_card.business_card.dto.OtpLoginRequest;
import com.business_card.business_card.model.User;
import com.business_card.business_card.repository.UserRepository;
import com.business_card.business_card.service.FileUploadService;
import com.business_card.business_card.service.UserService;
import com.business_card.business_card.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
//@CrossOrigin(origins = { "http://buisness-card-app.s3-website.ap-south-1.amazonaws.com","http://localhost:4200"})
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileUploadService fileUploadService;


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
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("Upload attempt: " + file.getOriginalFilename());

            // Absolute path for safety
            String uploadDirPath = new File("uploads").getAbsolutePath();
            File uploadDir = new File(uploadDirPath);
            System.out.println("Upload dir: " + uploadDirPath);

            // Ensure directory exists
            if (!uploadDir.exists()) {
                boolean created = uploadDir.mkdirs();
                System.out.println("Directory created: " + created);
            }

            // Generate unique filename
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File dest = new File(uploadDir, filename);
            System.out.println("💾 Saving to: " + dest.getAbsolutePath());

            file.transferTo(dest);

            String fileUrl = "http://buisness-card-app.s3-website.ap-south-1.amazonaws.com/" + filename;
            System.out.println("File saved! URL: " + fileUrl);
            return ResponseEntity.ok(Map.of("url", fileUrl));

        } catch (Exception e) {
            e.printStackTrace(); // Print full stack trace
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Upload failed: " + e.getMessage()));
        }
    }

}
