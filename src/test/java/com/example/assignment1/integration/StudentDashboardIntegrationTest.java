package com.example.assignment1.integration;

import com.example.assignment1.enitity.Department;
import com.example.assignment1.enitity.Role;
import com.example.assignment1.enitity.Student;
import com.example.assignment1.repository.DepartmentRepository;
import com.example.assignment1.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class StudentDashboardIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Student student;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        departmentRepository.deleteAll();

        Department dept = new Department();
        dept.setName("Computer Science");
        dept = departmentRepository.save(dept);

        student = new Student();
        student.setName("John Doe");
        student.setEmail("student@example.com");
        student.setPassword("password123");
        student.setRole(Role.STUDENT);
        student.setDepartment(dept);
        studentRepository.save(student);
    }

    @Test
    @WithMockUser(username = "student@example.com", roles = "STUDENT")
    void testStudentCanAccessDashboard() throws Exception {
        mockMvc.perform(get("/student/dashboard").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("student-dashboard"))
                .andExpect(model().attributeExists("student"));
    }

    @Test
    @WithMockUser(username = "teacher@example.com", roles = "TEACHER")
    void testTeacherCannotAccessStudentDashboard() throws Exception {
        mockMvc.perform(get("/student/dashboard").with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUnauthenticatedUserRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/student/dashboard"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "student@example.com", roles = "STUDENT")
    void testStudentDashboardDisplaysCorrectData() throws Exception {
        mockMvc.perform(get("/student/dashboard").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("student", student));
    }
}
