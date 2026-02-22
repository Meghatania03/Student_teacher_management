package com.example.assignment1.repository;

import com.example.assignment1.enitity.Course;
import com.example.assignment1.enitity.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Course course;
    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setName("Computer Science");
        department = departmentRepository.save(department);

        course = new Course();
        course.setName("Java Programming");
        course.setDepartment(department);
    }

    @Test
    void testSaveCourse() {
        Course savedCourse = courseRepository.save(course);
        assertNotNull(savedCourse.getId());
        assertEquals("Java Programming", savedCourse.getName());
        assertEquals("Computer Science", savedCourse.getDepartment().getName());
    }

    @Test
    void testFindById() {
        Course savedCourse = courseRepository.save(course);
        Optional<Course> foundCourse = courseRepository.findById(savedCourse.getId());
        assertTrue(foundCourse.isPresent());
        assertEquals("Java Programming", foundCourse.get().getName());
    }

    @Test
    void testUpdateCourse() {
        Course savedCourse = courseRepository.save(course);
        savedCourse.setName("Advanced Java Programming");
        Course updatedCourse = courseRepository.save(savedCourse);
        assertEquals("Advanced Java Programming", updatedCourse.getName());
    }

    @Test
    void testDeleteCourse() {
        Course savedCourse = courseRepository.save(course);
        Long courseId = savedCourse.getId();
        courseRepository.deleteById(courseId);
        Optional<Course> deletedCourse = courseRepository.findById(courseId);
        assertFalse(deletedCourse.isPresent());
    }

    @Test
    void testFindAllCourses() {
        courseRepository.save(course);
        Course course2 = new Course();
        course2.setName("Python Programming");
        course2.setDepartment(department);
        courseRepository.save(course2);

        assertEquals(2, courseRepository.findAll().size());
    }

    @Test
    void testCourseWithDepartment() {
        Course savedCourse = courseRepository.save(course);
        Optional<Course> foundCourse = courseRepository.findById(savedCourse.getId());
        assertTrue(foundCourse.isPresent());
        assertNotNull(foundCourse.get().getDepartment());
        assertEquals(department.getId(), foundCourse.get().getDepartment().getId());
    }
}
