package com.spring.controllers;


import com.spring.dtos.CustomPageResponse;
import com.spring.dtos.StudentDTO;
import com.spring.dtos.SubjectDTO;
import com.spring.services.StudentClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CustomPageResponse<StudentDTO>> getAllStudents(@RequestParam int page, @RequestParam int size) {
        CustomPageResponse<StudentDTO> studentPage = studentClientService.getAllStudents(page, size);
        CustomPageResponse<StudentDTO> response = new CustomPageResponse<>(
                studentPage.getContent(),
                studentPage.getTotalPages(),
                studentPage.getSize(),
                studentPage.getTotalElements()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/subjects/{name}")
    public List<SubjectDTO> getsubjectByName(@PathVariable String name) {
        return  studentClientService.findSubjectsByName(name);
    }
}