package com.business_card.business_card.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/test")
public class TestController {

        @GetMapping("/secure")
        public String secure() {
            return "You're authenticated and accessing a protected endpoint!";
        }
}
