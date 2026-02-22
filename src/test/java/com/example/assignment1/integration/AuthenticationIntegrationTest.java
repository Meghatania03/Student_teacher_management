package com.example.assignment1.integration;

import com.example.assignment1.enitity.Department;
import com.example.assignment1.enitity.Role;
import com.example.assignment1.enitity.Student;
import com.example.assignment1.enitity.Teacher;
import com.example.assignment1.repository.DepartmentRepository;
import com.example.assignment1.repository.StudentRepository;
import com.example.assignment1.repository.TeacherRepository;
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
class AuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Student student;
    private Teacher teacher;
    private Department department;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        teacherRepository.deleteAll();
        departmentRepository.deleteAll();

        department = new Department();
        department.setName("Computer Science");
        department = departmentRepository.save(department);

        student = new Student();
        student.setName("John Doe");
        student.setEmail("student@example.com");
        student.setPassword("password123");
        student.setRole(Role.STUDENT);
        student.setDepartment(department);
        studentRepository.save(student);

        teacher = new Teacher();
        teacher.setName("Mr. Smith");
        teacher.setEmail("teacher@example.com");
        teacher.setPassword("password123");
        teacher.setRole(Role.TEACHER);
        teacher.setDepartment(department);
        teacherRepository.save(teacher);
    }

    @Test
    @WithMockUser(username = "student@example.com", roles = "STUDENT")
    void testStudentCanAccessStudentResources() throws Exception {
        mockMvc.perform(get("/student/dashboard").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "student@example.com", roles = "STUDENT")
    void testStudentCannotAccessTeacherResources() throws Exception {
        mockMvc.perform(get("/teacher/dashboard").with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "teacher@example.com", roles = "TEACHER")
    void testTeacherCanAccessTeacherResources() throws Exception {
        mockMvc.perform(get("/teacher/dashboard").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "teacher@example.com", roles = "TEACHER")
    void testTeacherCannotAccessStudentResources() throws Exception {
        mockMvc.perform(get("/student/dashboard").with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUnauthenticatedUserCanAccessLoginPage() throws Exception {
        mockMvc.perform(get("/login").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testUnauthenticatedUserCannotAccessStudentDashboard() throws Exception {
        mockMvc.perform(get("/student/dashboard"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testUnauthenticatedUserCannotAccessTeacherDashboard() throws Exception {
        mockMvc.perform(get("/teacher/dashboard"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "student@example.com", roles = "STUDENT")
    void testStudentRoleAuthentication() throws Exception {
        mockMvc.perform(get("/student/dashboard").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("student"));
    }

    @Test
    @WithMockUser(username = "teacher@example.com", roles = "TEACHER")
    void testTeacherRoleAuthentication() throws Exception {
        mockMvc.perform(get("/teacher/dashboard").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("teacher"));
    }
}
