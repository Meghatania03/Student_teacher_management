package com.example.assignment1.repository;

import com.example.assignment1.enitity.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setName("Computer Science");
    }

    @Test
    void testSaveDepartment() {
        Department savedDept = departmentRepository.save(department);
        assertNotNull(savedDept.getId());
        assertEquals("Computer Science", savedDept.getName());
    }

    @Test
    void testFindById() {
        Department savedDept = departmentRepository.save(department);
        Optional<Department> foundDept = departmentRepository.findById(savedDept.getId());
        assertTrue(foundDept.isPresent());
        assertEquals("Computer Science", foundDept.get().getName());
    }

    @Test
    void testUpdateDepartment() {
        Department savedDept = departmentRepository.save(department);
        savedDept.setName("Information Technology");
        Department updatedDept = departmentRepository.save(savedDept);
        assertEquals("Information Technology", updatedDept.getName());
    }

    @Test
    void testDeleteDepartment() {
        Department savedDept = departmentRepository.save(department);
        Long deptId = savedDept.getId();
        departmentRepository.deleteById(deptId);
        Optional<Department> deletedDept = departmentRepository.findById(deptId);
        assertFalse(deletedDept.isPresent());
    }

    @Test
    void testFindAllDepartments() {
        departmentRepository.save(department);
        Department dept2 = new Department();
        dept2.setName("Engineering");
        departmentRepository.save(dept2);

        assertEquals(2, departmentRepository.findAll().size());
    }
}
