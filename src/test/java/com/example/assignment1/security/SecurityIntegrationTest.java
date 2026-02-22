package com.example.assignment1.security;

import com.example.assignment1.enitity.Role;
import com.example.assignment1.enitity.Student;
import com.example.assignment1.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class SecurityIntegrationTest {

    @Autowired
    private StudentRepository studentRepository;

    private Student student;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();

        student = new Student();
        student.setName("John Doe");
        student.setEmail("john@example.com");
        student.setPassword("password123");
        student.setRole(Role.STUDENT);
        studentRepository.save(student);
    }

    @Test
    void testFindStudentByEmail() {
        Optional<Student> foundStudent = studentRepository.findByEmail("john@example.com");
        assertTrue(foundStudent.isPresent());
        assertEquals("john@example.com", foundStudent.get().getEmail());
        assertEquals(Role.STUDENT, foundStudent.get().getRole());
    }

    @Test
    void testStudentNotFound() {
        Optional<Student> foundStudent = studentRepository.findByEmail("nonexistent@example.com");
        assertFalse(foundStudent.isPresent());
    }

    @Test
    void testStudentRoleIsStudent() {
        Optional<Student> foundStudent = studentRepository.findByEmail("john@example.com");
        assertTrue(foundStudent.isPresent());
        assertEquals(Role.STUDENT, foundStudent.get().getRole());
    }
}
