package com.example.assignment1.repository;

import com.example.assignment1.enitity.Role;
import com.example.assignment1.enitity.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TeacherRepositoryTest {

    @Autowired
    private TeacherRepository teacherRepository;

    private Teacher teacher;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setName("Mr. Smith");
        teacher.setEmail("smith@example.com");
        teacher.setPassword("password123");
        teacher.setRole(Role.TEACHER);
    }

    @Test
    void testSaveTeacher() {
        Teacher savedTeacher = teacherRepository.save(teacher);
        assertNotNull(savedTeacher.getId());
        assertEquals("Mr. Smith", savedTeacher.getName());
        assertEquals("smith@example.com", savedTeacher.getEmail());
    }

    @Test
    void testFindByEmail() {
        teacherRepository.save(teacher);
        Optional<Teacher> foundTeacher = teacherRepository.findByEmail("smith@example.com");
        assertTrue(foundTeacher.isPresent());
        assertEquals("Mr. Smith", foundTeacher.get().getName());
    }

    @Test
    void testFindByEmailNotFound() {
        Optional<Teacher> foundTeacher = teacherRepository.findByEmail("nonexistent@example.com");
        assertFalse(foundTeacher.isPresent());
    }

    @Test
    void testFindById() {
        Teacher savedTeacher = teacherRepository.save(teacher);
        Optional<Teacher> foundTeacher = teacherRepository.findById(savedTeacher.getId());
        assertTrue(foundTeacher.isPresent());
        assertEquals("Mr. Smith", foundTeacher.get().getName());
    }

    @Test
    void testUpdateTeacher() {
        Teacher savedTeacher = teacherRepository.save(teacher);
        savedTeacher.setName("Dr. Smith");
        Teacher updatedTeacher = teacherRepository.save(savedTeacher);
        assertEquals("Dr. Smith", updatedTeacher.getName());
    }

    @Test
    void testDeleteTeacher() {
        Teacher savedTeacher = teacherRepository.save(teacher);
        Long teacherId = savedTeacher.getId();
        teacherRepository.deleteById(teacherId);
        Optional<Teacher> deletedTeacher = teacherRepository.findById(teacherId);
        assertFalse(deletedTeacher.isPresent());
    }

    @Test
    void testFindAllTeachers() {
        teacherRepository.save(teacher);
        Teacher teacher2 = new Teacher();
        teacher2.setName("Mrs. Johnson");
        teacher2.setEmail("johnson@example.com");
        teacher2.setPassword("password123");
        teacher2.setRole(Role.TEACHER);
        teacherRepository.save(teacher2);

        assertEquals(2, teacherRepository.findAll().size());
    }
}
