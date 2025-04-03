package com.business_card.business_card.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String businessName;
    private String description;

    private String phone;
    private String email;
    private String address;

    private String facebookUrl;
    private String instagramUrl;
    private String linkedinUrl;
    private String youtubeUrl;

    private String googleMapLink;
    private String paymentQrUrl;
    private String logoUrl;

    private String publicUrl;

    @ManyToOne
    @JsonBackReference
    private User user;

}

