package com.spring.controllers;


import com.spring.dtos.StudentDTO;
import com.spring.dtos.SubjectDTO;
import com.spring.services.StudentClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/students")
public class StudentClientController {
    @Autowired
    private StudentClientService studentClientService;
    @GetMapping("/name/{name}")
    public List<StudentDTO> getStudentByName(@PathVariable String name) {
        return studentClientService.getStudentByName(name);
    }
    @GetMapping("/age/{age}")
    public List<StudentDTO> getStudentByAge(@PathVariable int age) {
        return studentClientService.getStudentByAge(age);
    }
    @GetMapping("/all")
    public List<StudentDTO> getAllStudents() {
        return studentClientService.getAllStudents();
    }

    @GetMapping("/subject/{name}")
    public List<SubjectDTO> getsubjectByName(@PathVariable String name) {
        return  studentClientService.findsubjectByName(name);
    }
}

