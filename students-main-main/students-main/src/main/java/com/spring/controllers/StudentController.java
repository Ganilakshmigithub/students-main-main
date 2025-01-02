package com.spring.controllers;
import com.spring.dtos.StudentDTO;
import com.spring.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    // Add a new student
    @PostMapping("/save")
    public ResponseEntity<?> addStudent(@RequestBody @Valid StudentDTO student, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse<StudentDTO>("validation failed..please check deatils before you post!!"));
            studentService.addStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED).body(student);
    }
    //update student marks
    @PutMapping("/update/{student_id}/{subject_id}/newmarks")
    public String UpdateMarks(@PathVariable int student_id, @PathVariable int subject_id, @RequestParam int newmarks) {
        studentService.updateMarks(student_id, subject_id, newmarks);
        return "marks updated successfully..!!";
    }

    //deletes student by id
    @DeleteMapping("/del/{id}")
    public void deleteStudentById(@PathVariable int id) {
        studentService.deleteStudentById(id);
    }

}
