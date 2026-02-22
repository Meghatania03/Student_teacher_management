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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class StudentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Student student;
    private Department department;

    @BeforeEach
    void setUp() {
        // Clear existing data
        studentRepository.deleteAll();
        departmentRepository.deleteAll();

        // Create department
        department = new Department();
        department.setName("Computer Science");
        department = departmentRepository.save(department);

        // Create student
        student = new Student();
        student.setName("John Doe");
        student.setEmail("john@example.com");
        student.setPassword("password123");
        student.setRole(Role.STUDENT);
        student.setDepartment(department);
        student = studentRepository.save(student);
    }

    @Test
    void testCreateStudentWithDepartment() {
        Optional<Student> foundStudent = studentRepository.findByEmail("john@example.com");
        assertTrue(foundStudent.isPresent());
        assertEquals("John Doe", foundStudent.get().getName());
        assertNotNull(foundStudent.get().getDepartment());
        assertEquals("Computer Science", foundStudent.get().getDepartment().getName());
    }

    @Test
    void testUpdateStudentDepartment() {
        Department newDept = new Department();
        newDept.setName("Engineering");
        newDept = departmentRepository.save(newDept);

        student.setDepartment(newDept);
        studentRepository.save(student);

        Optional<Student> updatedStudent = studentRepository.findByEmail("john@example.com");
        assertTrue(updatedStudent.isPresent());
        assertEquals("Engineering", updatedStudent.get().getDepartment().getName());
    }

    @Test
    void testStudentAndDepartmentRelationship() {
        // Verify the relationship
        Optional<Student> foundStudent = studentRepository.findByEmail("john@example.com");
        assertTrue(foundStudent.isPresent());

        Student retrievedStudent = foundStudent.get();
        assertNotNull(retrievedStudent.getDepartment());
        assertEquals(department.getId(), retrievedStudent.getDepartment().getId());
    }

    @Test
    void testMultipleStudentsInSameDepartment() {
        Student student2 = new Student();
        student2.setName("Jane Doe");
        student2.setEmail("jane@example.com");
        student2.setPassword("password123");
        student2.setRole(Role.STUDENT);
        student2.setDepartment(department);
        studentRepository.save(student2);

        assertEquals(2, studentRepository.findAll().size());
        assertEquals(2, studentRepository.findAll().stream()
                .filter(s -> s.getDepartment().getId().equals(department.getId()))
                .count());
    }

    @Test
    void testRemoveStudentDepartment() {
        student.setDepartment(null);
        studentRepository.save(student);

        Optional<Student> updatedStudent = studentRepository.findByEmail("john@example.com");
        assertTrue(updatedStudent.isPresent());
        assertNull(updatedStudent.get().getDepartment());
    }
}
