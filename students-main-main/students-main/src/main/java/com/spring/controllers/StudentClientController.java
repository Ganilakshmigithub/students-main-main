package com.spring.controllers;


import ch.qos.logback.core.net.SyslogOutputStream;
import com.spring.dtos.ApiResponse;
import com.spring.dtos.CustomPageResponse;
import com.spring.dtos.StudentDTO;
import com.spring.dtos.SubjectDTO;
import com.spring.exceptions.StudentNotFoundException;
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
    public ResponseEntity<?> getStudentByName(@PathVariable String name) {
        if (studentClientService.getStudentsByName(name).isEmpty()) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Student Not Found with the name",HttpStatus.NOT_FOUND.value()));
        }
        return new ResponseEntity<>(studentClientService.getStudentsByName(name), HttpStatus.OK);
    }

    @GetMapping("/age/{age}")
    public ResponseEntity<?> getStudentByAge(@PathVariable int age) {
        if(studentClientService.getStudentByAge(age).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Student Not Found with the given age",HttpStatus.NOT_FOUND.value()));
        }
        return new ResponseEntity<>(studentClientService.getStudentByAge(age), HttpStatus.OK);
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
    public ResponseEntity<?> getsubjectByName(@PathVariable String name) {
        if(studentClientService.findSubjectsByName(name).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Subject Not Found with the name",HttpStatus.NOT_FOUND.value()));
        }
        return new ResponseEntity<>(studentClientService.findSubjectsByName(name), HttpStatus.OK);
    }
}