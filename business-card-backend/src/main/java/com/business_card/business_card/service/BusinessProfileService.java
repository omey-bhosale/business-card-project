package com.business_card.business_card.service;

import com.business_card.business_card.model.BusinessProfile;
import com.business_card.business_card.model.User;
import com.business_card.business_card.repository.BusinessProfileRepository;
import com.business_card.business_card.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusinessProfileService {

    @Autowired
    private BusinessProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    public BusinessProfile createProfile(BusinessProfile profile, String phone) {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (profileRepository.countByUser(user) >= 3) {
            throw new RuntimeException("Maximum 3 business profiles allowed.");
        }

        profile.setUser(user);
        // You can generate a unique public URL here
        profile.setPublicUrl("https://card.domain.com/" + System.currentTimeMillis());

        return profileRepository.save(profile);
    }

    public List<BusinessProfile> getProfiles(String phone) {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return profileRepository.findByUser(user);
    }
}
