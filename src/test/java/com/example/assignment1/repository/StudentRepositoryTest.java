package com.example.assignment1.repository;

import com.example.assignment1.enitity.Role;
import com.example.assignment1.enitity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setName("John Doe");
        student.setEmail("john@example.com");
        student.setPassword("password123");
        student.setRole(Role.STUDENT);
    }

    @Test
    void testSaveStudent() {
        Student savedStudent = studentRepository.save(student);
        assertNotNull(savedStudent.getId());
        assertEquals("John Doe", savedStudent.getName());
        assertEquals("john@example.com", savedStudent.getEmail());
    }

    @Test
    void testFindByEmail() {
        studentRepository.save(student);
        Optional<Student> foundStudent = studentRepository.findByEmail("john@example.com");
        assertTrue(foundStudent.isPresent());
        assertEquals("John Doe", foundStudent.get().getName());
    }

    @Test
    void testFindByEmailNotFound() {
        Optional<Student> foundStudent = studentRepository.findByEmail("nonexistent@example.com");
        assertFalse(foundStudent.isPresent());
    }

    @Test
    void testFindById() {
        Student savedStudent = studentRepository.save(student);
        Optional<Student> foundStudent = studentRepository.findById(savedStudent.getId());
        assertTrue(foundStudent.isPresent());
        assertEquals("John Doe", foundStudent.get().getName());
    }

    @Test
    void testUpdateStudent() {
        Student savedStudent = studentRepository.save(student);
        savedStudent.setName("Jane Doe");
        Student updatedStudent = studentRepository.save(savedStudent);
        assertEquals("Jane Doe", updatedStudent.getName());
    }

    @Test
    void testDeleteStudent() {
        Student savedStudent = studentRepository.save(student);
        Long studentId = savedStudent.getId();
        studentRepository.deleteById(studentId);
        Optional<Student> deletedStudent = studentRepository.findById(studentId);
        assertFalse(deletedStudent.isPresent());
    }

    @Test
    void testFindAllStudents() {
        studentRepository.save(student);
        Student student2 = new Student();
        student2.setName("Jane Doe");
        student2.setEmail("jane@example.com");
        student2.setPassword("password123");
        student2.setRole(Role.STUDENT);
        studentRepository.save(student2);

        assertEquals(2, studentRepository.findAll().size());
    }
}
