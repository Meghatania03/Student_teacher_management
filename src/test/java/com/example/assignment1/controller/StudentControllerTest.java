package com.example.assignment1.controller;

import com.example.assignment1.enitity.Role;
import com.example.assignment1.enitity.Student;
import com.example.assignment1.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setName("John Doe");
        student.setEmail("john@example.com");
        student.setPassword("password123");
        student.setRole(Role.STUDENT);
    }

    @Test
    @WithMockUser(username = "john@example.com", roles = "STUDENT")
    void testStudentDashboard() throws Exception {
        when(studentRepository.findByEmail("john@example.com")).thenReturn(Optional.of(student));

        mockMvc.perform(get("/student/dashboard").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("student-dashboard"))
                .andExpect(model().attributeExists("student"))
                .andExpect(model().attribute("student", student));
    }

    @Test
    @WithMockUser(username = "nonexistent@example.com", roles = "STUDENT")
    void testStudentDashboardStudentNotFound() throws Exception {
        when(studentRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(get("/student/dashboard").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("student-dashboard"))
                .andExpect(model().attribute("student", null));
    }

    @Test
    void testStudentDashboardWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/student/dashboard"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "teacher@example.com", roles = "TEACHER")
    void testStudentDashboardWithTeacherRole() throws Exception {
        mockMvc.perform(get("/student/dashboard").with(csrf()))
                .andExpect(status().isForbidden());
    }
}
