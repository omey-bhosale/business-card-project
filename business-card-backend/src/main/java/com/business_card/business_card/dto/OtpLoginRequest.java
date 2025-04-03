package com.business_card.business_card.dto;

import lombok.Data;

@Data
public class OtpLoginRequest {
    private String phone;
    private String otp;
}
