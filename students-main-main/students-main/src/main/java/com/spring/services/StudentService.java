package com.spring.services;
import com.spring.dtos.StudentDTO;
import com.spring.dtos.SubjectDTO;
import com.spring.entities.Students;
import com.spring.entities.Subject;
import com.spring.repos.StudentRepo;
import com.spring.repos.SubjectRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class StudentService {
    @Autowired
    private StudentRepo studentRepo;
    @Autowired
    private SubjectRepo subjectRepo;
    // Convert Students to StudentDTO
    private StudentDTO convertToDTO(Students student) {
        List<SubjectDTO> subjectDTOs = student.getSubjects().stream()
                .map(subject -> new SubjectDTO(subject.getSubId(), subject.getName(), subject.getMarks()))
                .collect(Collectors.toList());
        return new StudentDTO(student.getId(),student.getName(), student.getAge(), student.getGender(),
                student.getDob(), student.getCourse(), student.getCourseStartYear(), student.getCourseEndYear(), subjectDTOs);
    }
    // Add a new student
    public Students addStudent(Students student) {
        return studentRepo.save(student);
    }


    //update student marks
    @Transactional
    public void updateMarks(int student_id,int subject_id,int newmarks){

        Students s=studentRepo.findById(student_id).orElseThrow(null);
        for(Subject subject:s.getSubjects()){
            if(subject.getSubId()==subject_id){
                subject.setMarks(newmarks);
                break;
            }
        }
        studentRepo.save(s);
    }

    //delete student by id
    public void deleteStudentById(int id){
         studentRepo.deleteById(id);
    }
}

