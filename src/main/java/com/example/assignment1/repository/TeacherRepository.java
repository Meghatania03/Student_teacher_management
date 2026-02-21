package com.example.assignment1.repository;

import com.example.assignment1.enitity.Student;
import com.example.assignment1.enitity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByEmail(String email);
}
