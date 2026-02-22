package com.example.assignment1.integration;

import com.example.assignment1.enitity.*;
import com.example.assignment1.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EndToEndIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Student student;
    private Teacher teacher;
    private Department department;
    private Course course;

    @BeforeEach
    void setUp() {
        // Clear all data
        studentRepository.deleteAll();
        teacherRepository.deleteAll();
        courseRepository.deleteAll();
        departmentRepository.deleteAll();

        // Create Department
        department = new Department();
        department.setName("Computer Science");
        department = departmentRepository.save(department);

        // Create Course
        course = new Course();
        course.setName("Data Structures");
        course.setDepartment(department);
        course = courseRepository.save(course);

        // Create Student
        student = new Student();
        student.setName("John Doe");
        student.setEmail("john@example.com");
        student.setPassword("password123");
        student.setRole(Role.STUDENT);
        student.setDepartment(department);
        student.setCourses(Arrays.asList(course));
        student = studentRepository.save(student);

        // Create Teacher
        teacher = new Teacher();
        teacher.setName("Dr. Smith");
        teacher.setEmail("smith@example.com");
        teacher.setPassword("password123");
        teacher.setRole(Role.TEACHER);
        teacher.setDepartment(department);
        teacher = teacherRepository.save(teacher);
    }

    @Test
    void testDepartmentCreatedSuccessfully() {
        Optional<Department> foundDept = departmentRepository.findById(department.getId());
        assertTrue(foundDept.isPresent());
        assertEquals("Computer Science", foundDept.get().getName());
    }

    @Test
    void testCourseCreatedUnderDepartment() {
        Optional<Course> foundCourse = courseRepository.findById(course.getId());
        assertTrue(foundCourse.isPresent());
        assertEquals("Data Structures", foundCourse.get().getName());
        assertEquals("Computer Science", foundCourse.get().getDepartment().getName());
    }

    @Test
    void testStudentAssignedToDepartmentAndCourse() {
        Optional<Student> foundStudent = studentRepository.findByEmail("john@example.com");
        assertTrue(foundStudent.isPresent());

        Student retrievedStudent = foundStudent.get();
        assertNotNull(retrievedStudent.getDepartment());
        assertEquals("Computer Science", retrievedStudent.getDepartment().getName());
        assertNotNull(retrievedStudent.getCourses());
        assertEquals(1, retrievedStudent.getCourses().size());
        assertEquals("Data Structures", retrievedStudent.getCourses().get(0).getName());
    }

    @Test
    void testTeacherAssignedToDepartment() {
        Optional<Teacher> foundTeacher = teacherRepository.findByEmail("smith@example.com");
        assertTrue(foundTeacher.isPresent());

        Teacher retrievedTeacher = foundTeacher.get();
        assertNotNull(retrievedTeacher.getDepartment());
        assertEquals("Computer Science", retrievedTeacher.getDepartment().getName());
    }

    @Test
    void testMultipleStudentsInSameDepartmentAndCourse() {
        // Create another student
        Student student2 = new Student();
        student2.setName("Jane Doe");
        student2.setEmail("jane@example.com");
        student2.setPassword("password123");
        student2.setRole(Role.STUDENT);
        student2.setDepartment(department);
        student2.setCourses(Arrays.asList(course));
        studentRepository.save(student2);

        // Verify both students are in the same department and course
        long countInDept = studentRepository.findAll().stream()
                .filter(s -> s.getDepartment().getId().equals(department.getId()))
                .count();
        assertEquals(2, countInDept);
    }

    @Test
    @WithMockUser(username = "john@example.com", roles = "STUDENT")
    void testStudentCanAccessDashboardWithValidData() throws Exception {
        mockMvc.perform(get("/student/dashboard").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("student-dashboard"))
                .andExpect(model().attributeExists("student"));
    }

    @Test
    @WithMockUser(username = "smith@example.com", roles = "TEACHER")
    void testTeacherCanAccessDashboardWithValidData() throws Exception {
        mockMvc.perform(get("/teacher/dashboard").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher-dashboard"))
                .andExpect(model().attributeExists("teacher"));
    }

    @Test
    @WithMockUser(username = "john@example.com", roles = "STUDENT")
    void testStudentRoleIssCorrect() {
        Optional<Student> foundStudent = studentRepository.findByEmail("john@example.com");
        assertTrue(foundStudent.isPresent());
        assertEquals(Role.STUDENT, foundStudent.get().getRole());
    }

    @Test
    @WithMockUser(username = "smith@example.com", roles = "TEACHER")
    void testTeacherRoleIsCorrect() {
        Optional<Teacher> foundTeacher = teacherRepository.findByEmail("smith@example.com");
        assertTrue(foundTeacher.isPresent());
        assertEquals(Role.TEACHER, foundTeacher.get().getRole());
    }

    @Test
    void testRelationshipBetweenAllEntities() {
        // Verify the complete relationship chain
        Optional<Student> foundStudent = studentRepository.findByEmail("john@example.com");
        assertTrue(foundStudent.isPresent());

        Student retrievedStudent = foundStudent.get();

        // Check student-department relationship
        assertNotNull(retrievedStudent.getDepartment());
        Department deptFromStudent = retrievedStudent.getDepartment();
        assertEquals(department.getId(), deptFromStudent.getId());

        // Check student-course relationship
        assertNotNull(retrievedStudent.getCourses());
        assertTrue(retrievedStudent.getCourses().size() > 0);
        Course courseFromStudent = retrievedStudent.getCourses().get(0);
        assertEquals(course.getId(), courseFromStudent.getId());

        // Check course-department relationship
        assertEquals(department.getId(), courseFromStudent.getDepartment().getId());
    }

    @Test
    void testDataIntegrityAcrossRepositories() {
        // Verify data is correctly saved and retrievable across repositories
        assertEquals(1, departmentRepository.findAll().size());
        assertEquals(1, courseRepository.findAll().size());
        assertEquals(1, studentRepository.findAll().size());
        assertEquals(1, teacherRepository.findAll().size());

        // Verify references are correct
        Course retrievedCourse = courseRepository.findAll().get(0);
        assertEquals("Computer Science", retrievedCourse.getDepartment().getName());

        Student retrievedStudent = studentRepository.findAll().get(0);
        assertEquals("Computer Science", retrievedStudent.getDepartment().getName());

        Teacher retrievedTeacher = teacherRepository.findAll().get(0);
        assertEquals("Computer Science", retrievedTeacher.getDepartment().getName());
    }
}
