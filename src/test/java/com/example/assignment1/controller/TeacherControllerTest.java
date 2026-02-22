package com.example.assignment1.controller;

import com.example.assignment1.enitity.Role;
import com.example.assignment1.enitity.Teacher;
import com.example.assignment1.repository.TeacherRepository;
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
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherRepository teacherRepository;

    private Teacher teacher;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("Mr. Smith");
        teacher.setEmail("smith@example.com");
        teacher.setPassword("password123");
        teacher.setRole(Role.TEACHER);
    }

    @Test
    @WithMockUser(username = "smith@example.com", roles = "TEACHER")
    void testTeacherDashboard() throws Exception {
        when(teacherRepository.findByEmail("smith@example.com")).thenReturn(Optional.of(teacher));

        mockMvc.perform(get("/teacher/dashboard").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher-dashboard"))
                .andExpect(model().attributeExists("teacher"))
                .andExpect(model().attribute("teacher", teacher));
    }

    @Test
    @WithMockUser(username = "nonexistent@example.com", roles = "TEACHER")
    void testTeacherDashboardTeacherNotFound() throws Exception {
        when(teacherRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(get("/teacher/dashboard").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher-dashboard"))
                .andExpect(model().attribute("teacher", null));
    }

    @Test
    void testTeacherDashboardWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/teacher/dashboard"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "student@example.com", roles = "STUDENT")
    void testTeacherDashboardWithStudentRole() throws Exception {
        mockMvc.perform(get("/teacher/dashboard").with(csrf()))
                .andExpect(status().isForbidden());
    }
}
