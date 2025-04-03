package com.business_card.business_card.repository;

import com.business_card.business_card.model.BusinessProfile;
import com.business_card.business_card.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusinessProfileRepository extends JpaRepository<BusinessProfile, Long> {
    List<BusinessProfile> findByUser(User user);
    int countByUser(User user);
}
