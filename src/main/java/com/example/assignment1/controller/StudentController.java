package com.example.assignment1.controller;

import com.example.assignment1.enitity.Student;
import com.example.assignment1.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StudentRepository repo;

    @GetMapping("/dashboard")
    public String studentDashboard(Model model, Authentication authentication) {
        String email = authentication.getName();
        Student student = repo.findByEmail(email).orElse(null);
        model.addAttribute("student", student);
        return "student-dashboard";
    }
}
