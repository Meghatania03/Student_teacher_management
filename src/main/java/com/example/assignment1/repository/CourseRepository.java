package com.example.assignment1.repository;

import com.example.assignment1.enitity.Course;
import com.example.assignment1.enitity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

}
