package com.spring.services;
import com.spring.entities.Students;
import com.spring.entities.Subject;
import com.spring.repos.StudentRepo;
import com.spring.exceptions.StudentNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class StudentService {
    @Autowired
    private StudentRepo studentRepo;
    // Add a new student
    public Students addStudent(Students student) {
        return studentRepo.save(student);
    }
    // Update student marks
    @Transactional
    public void updateMarks(int studentId, int subjectId, int newMarks) {
        Students student = studentRepo.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student with id " + studentId + " not found"));
        boolean subjectFound = false;
        for (Subject subject : student.getSubjects()) {
            if (subject.getSubId() == subjectId) {
                subject.setMarks(newMarks);
                subjectFound = true;
                break;
            }
        }
        if (!subjectFound) {
            throw new StudentNotFoundException("Subject with id " + subjectId + " not found for student with id " + studentId);
        }
        studentRepo.save(student);
    }
    // Delete student by id
    public void deleteStudentById(int id) {
        if (studentRepo.existsById(id)) {
            studentRepo.deleteById(id);
        } else {
            throw new StudentNotFoundException("Student with id " + id + " not found");
        }
    }
}

