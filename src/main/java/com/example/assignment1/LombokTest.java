package com.example.assignment1;



import com.example.assignment1.enitity.Student;
import com.example.assignment1.enitity.Teacher;

public class LombokTest {
    public static void main(String[] args) {
        Student t = new Student();
        t.setName("John");       // Should work if Lombok is working
        t.setPassword("123");    // Should work too
        System.out.println(t.getName());
        System.out.println(t.getPassword());
    }
}

