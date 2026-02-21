package com.example.assignment1.repository;

import com.example.assignment1.enitity.Department;
import com.example.assignment1.enitity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

}
