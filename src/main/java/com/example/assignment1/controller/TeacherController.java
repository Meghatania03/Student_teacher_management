package com.example.assignment1.controller;

import com.example.assignment1.enitity.*;
import com.example.assignment1.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("/teacher")
public class TeacherController {

    private final StudentRepository studentRepo;
    private final TeacherRepository teacherRepo;
    private final DepartmentRepository deptRepo;
    private final CourseRepository courseRepo;
    private final PasswordEncoder passwordEncoder;

    public TeacherController(StudentRepository studentRepo, TeacherRepository teacherRepo,
                             DepartmentRepository deptRepo, CourseRepository courseRepo,
                             PasswordEncoder passwordEncoder) {
        this.studentRepo = studentRepo;
        this.teacherRepo = teacherRepo;
        this.deptRepo = deptRepo;
        this.courseRepo = courseRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // ===== DASHBOARD =====
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        String email = authentication.getName();
        Teacher teacher = teacherRepo.findByEmail(email).orElse(null);
        model.addAttribute("teacher", teacher);
        model.addAttribute("students", studentRepo.findAll());
        model.addAttribute("teachers", teacherRepo.findAll());
        model.addAttribute("departments", deptRepo.findAll());
        model.addAttribute("courses", courseRepo.findAll());
        return "teacher-dashboard";
    }

    // ================== STUDENT CRUD ==================
    @GetMapping("/student/add")
    public String addStudentForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("departments", deptRepo.findAll());
        model.addAttribute("courses", courseRepo.findAll());
        return "add-student";
    }

    @PostMapping("/student/add")
    public String addStudent(@ModelAttribute Student student) {
        student.setRole(Role.STUDENT);
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        studentRepo.save(student);
        return "redirect:/teacher/dashboard";
    }

    @GetMapping("/student/edit/{id}")
    public String editStudentForm(@PathVariable Long id, Model model) {
        Student student = studentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));
        model.addAttribute("student", student);
        model.addAttribute("departments", deptRepo.findAll());
        model.addAttribute("courses", courseRepo.findAll());
        return "edit-student";
    }

    @PostMapping("/student/update/{id}")
    public String updateStudent(@PathVariable Long id, @ModelAttribute Student student) {
        student.setId(id);
        student.setRole(Role.STUDENT);
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        studentRepo.save(student);
        return "redirect:/teacher/dashboard";
    }

    @GetMapping("/student/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentRepo.deleteById(id);
        return "redirect:/teacher/dashboard";
    }

    // ================== TEACHER CRUD ==================
    @GetMapping("/teacher/add")
    public String addTeacherForm(Model model) {
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("departments", deptRepo.findAll());
        return "add-teacher";
    }

    @PostMapping("/teacher/add")
    public String addTeacher(@ModelAttribute Teacher teacher) {
        teacher.setRole(Role.TEACHER);
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        teacherRepo.save(teacher);
        return "redirect:/teacher/dashboard";
    }

    @GetMapping("/teacher/edit/{id}")
    public String editTeacherForm(@PathVariable Long id, Model model) {
        Teacher teacher = teacherRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid teacher Id:" + id));
        model.addAttribute("teacher", teacher);
        model.addAttribute("departments", deptRepo.findAll());
        return "edit-teacher";
    }

    @PostMapping("/teacher/update/{id}")
    public String updateTeacher(@PathVariable Long id, @ModelAttribute Teacher teacher) {
        teacher.setId(id);
        teacher.setRole(Role.TEACHER);
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        teacherRepo.save(teacher);
        return "redirect:/teacher/dashboard";
    }

    @GetMapping("/teacher/delete/{id}")
    public String deleteTeacher(@PathVariable Long id) {
        teacherRepo.deleteById(id);
        return "redirect:/teacher/dashboard";
    }

    // ================== DEPARTMENT CRUD ==================
    @GetMapping("/department/add")
    public String addDepartmentForm(Model model) {
        model.addAttribute("department", new Department());
        return "add-department";
    }

    @PostMapping("/department/add")
    public String addDepartment(@ModelAttribute Department department) {
        deptRepo.save(department);
        return "redirect:/teacher/dashboard";
    }

    // ================== COURSE CRUD ==================
    @GetMapping("/course/add")
    public String addCourseForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("departments", deptRepo.findAll());
        return "add-course";
    }

    @PostMapping("/course/add")
    public String addCourse(@ModelAttribute Course course) {
        courseRepo.save(course);
        return "redirect:/teacher/dashboard";
    }
}
