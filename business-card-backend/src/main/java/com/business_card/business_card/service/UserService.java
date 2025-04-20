package com.business_card.business_card.service;

import com.business_card.business_card.repository.UserRepository;
import com.business_card.business_card.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public String sendOtp(String phone) {
        String otp = String.valueOf(new Random().nextInt(9000) + 1000);

        // Create or update user with OTP for login
        User user = userRepository.findByPhone(phone).orElseGet(() ->
                User.builder()
                        .phone(phone)
                        .isVerified(false)  // login may not need verified flag
                        .build()
        );

        user.setOtp(otp);
        userRepository.save(user);

        System.out.println("OTP for " + phone + ": " + otp); // dev only
        return otp;
    }



    public boolean verifyOtp(String phone, String inputOtp) {
        return userRepository.findByPhone(phone)
                .filter(user -> user.getOtp().equals(inputOtp))
                .map(user -> {
                    user.setVerified(true);
                    user.setOtp(null); // Optional: clear OTP after verification
                    userRepository.save(user);
                    return true;
                }).orElse(false);
    }

    public User registerUser(User user) {
        Optional<User> existingUser = userRepository.findByPhone(user.getPhone());

        if (existingUser.isPresent()) {
            User existing = existingUser.get();
            if (!existing.isVerified()) {
                throw new RuntimeException("Phone number not verified. Please verify OTP first.");
            }
            // If verified, update user details & password
            existing.setName(user.getName());
            existing.setEmail(user.getEmail());
            existing.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(existing);
        } else {
            throw new RuntimeException("Phone number not found or OTP not sent.");
        }
    }


    public Optional<User> loginWithEmail(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()) && user.isVerified());
    }

    public Optional<User> loginWithOtp(String phone, String otp) {
        Optional<User> userOpt = userRepository.findByPhone(phone);
        if (userOpt.isEmpty()) {
            System.out.println("Phone not found: " + phone);
            return Optional.empty();
        }

        User user = userOpt.get();
        System.out.println("Found user: " + user.getPhone() + ", OTP in DB: " + user.getOtp());

        if (!user.isVerified()) {
            System.out.println("User not verified");
            return Optional.empty();
        }

        if (!otp.equals(user.getOtp())) {
            System.out.println("OTP mismatch: received " + otp);
            return Optional.empty();
        }

        return Optional.of(user);
    }

    public Optional<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }


}
