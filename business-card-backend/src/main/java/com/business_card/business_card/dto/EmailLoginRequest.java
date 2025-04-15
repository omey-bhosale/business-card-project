package com.business_card.business_card.dto;

import lombok.Data;

@Data
public class EmailLoginRequest {
    private String email; 
    private String password;     
}

