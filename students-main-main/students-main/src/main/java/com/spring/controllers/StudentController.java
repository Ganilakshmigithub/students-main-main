package com.spring.controllers;
import com.spring.entities.Students;
import com.spring.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;
    // Add a new student
    @PostMapping("/save")
    public Students addStudent(@RequestBody Students student) {
        return studentService.addStudent(student);
    }


    //update student marks
    @PutMapping("/update/{student_id}/{subject_id}/newmarks")
    public String UpdateMarks(@PathVariable int student_id,@PathVariable int subject_id,@RequestParam int newmarks){
        studentService.updateMarks(student_id, subject_id, newmarks);
        return "marks updated successfully..!!";
    }

    //deletes student by id
    @DeleteMapping("/del/{id}")
    public void deleteStudentById(@PathVariable int id){
        studentService.deleteStudentById(id);
    }

}

