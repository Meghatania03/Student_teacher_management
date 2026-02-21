package com.example.assignment1.controller;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/redirect")
    public String redirectAfterLogin(Authentication auth) {
        if(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER"))) {
            return "redirect:/teacher/dashboard";
        } else {
            return "redirect:/student/dashboard";
        }
    }

    // You can also put other mappings here
    @GetMapping("/login")
    public String login() {
        return "login";  // your login.html page
    }
}

