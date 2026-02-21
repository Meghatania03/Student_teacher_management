package com.example.assignment1.security;

import com.example.assignment1.enitity.Student;
import com.example.assignment1.enitity.Teacher;
import com.example.assignment1.repository.StudentRepository;
import com.example.assignment1.repository.TeacherRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;



import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    StudentRepository studentRepo;

    @Autowired
    TeacherRepository teacherRepo;

    @Override
    public UserDetails loadUserByUsername(String email) {

        if (studentRepo.findByEmail(email).isPresent()) {
            Student s = studentRepo.findByEmail(email).get();
            return new User(
                    s.getEmail(),
                    s.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_" + s.getRole()))
            );
        }

        if (teacherRepo.findByEmail(email).isPresent()) {
            Teacher t = teacherRepo.findByEmail(email).get();
            return new User(
                    t.getEmail(),
                    t.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_" + t.getRole()))
            );
        }

        throw new UsernameNotFoundException("User not found");
    }
}

