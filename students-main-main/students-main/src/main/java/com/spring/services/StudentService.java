package com.spring.services;
import com.spring.dtos.StudentDTO;
import com.spring.dtos.SubjectDTO;
import com.spring.entities.Students;
import com.spring.entities.Subject;
import com.spring.repos.StudentRepo;
import com.spring.exceptions.StudentNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    ;
    @Autowired
    private StudentRepo studentRepo;

    public StudentDTO convertToDTO(Students student) {
        List<SubjectDTO> subjectDTOs = student.getSubjects().stream()
                .map(subject -> new SubjectDTO(subject.getSubId(), subject.getName(), subject.getMarks()))
                .collect(Collectors.toList());
        return new StudentDTO(student.getId(),student.getName(), student.getAge(), student.getGender(),
                student.getDob(), student.getCourse(), student.getCourseStartYear(), student.getCourseEndYear(), subjectDTOs);
    }
    public Students convertToEntity(StudentDTO studentDTO){
        Students students = new Students();
        students.setId(studentDTO.getId());
        students.setName(studentDTO.getName());
        students.setAge(studentDTO.getAge());
        students.setGender(studentDTO.getGender());
        students.setDob(studentDTO.getDob());
        students.setCourse(studentDTO.getCourse());
        students.setCourseStartYear(studentDTO.getCourseStartYear());
        students.setCourseEndYear(studentDTO.getCourseEndYear());
        if (studentDTO.getSubjects() != null) {
            List<Subject> subjects = studentDTO.getSubjects().stream()
                    .map(subjectDTO -> {
                        Subject subject = new Subject();
                        subject.setSubId(subjectDTO.getSubId());
                        subject.setName(subjectDTO.getName());
                        subject.setMarks(subjectDTO.getMarks());
                        return subject;
                    })
                    .collect(Collectors.toList());
            students.setSubjects(subjects);
        }
        return students;
    }
    // Add a new student
    public StudentDTO addStudent(StudentDTO studentDTO) {
        Students students = convertToEntity(studentDTO);
        studentRepo.save(students);
        return convertToDTO(students);
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

