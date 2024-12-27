package com.spring.services;

import com.spring.dtos.StudentDTO;
import com.spring.dtos.SubjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentClientService {
    private static final String STUDENT_SERVICE_URL = "http://localhost:8083/students";

    @Autowired
    private RestTemplate restTemplate;
    // Fetch students by name
    public List<StudentDTO> getStudentByName(String name) {
        String url = STUDENT_SERVICE_URL + "/name/" + name;
        List<StudentDTO> students = restTemplate.getForObject(url, List.class);   //get for object is getmapping
        return students;
    }
    // Fetch students by age
    public List<StudentDTO> getStudentByAge(int age) {
        String url = STUDENT_SERVICE_URL + "/age/" + age;
        List<StudentDTO> students = restTemplate.getForObject(url, List.class);
        return students;
    }
    // Fetch all students
    public List<StudentDTO> getAllStudents() {
        String url = STUDENT_SERVICE_URL + "/all?page=0&size=2";
        // Customize pagination as needed
        List<StudentDTO> students = restTemplate.getForObject(url, List.class);
        return students;
    }

    //fetch subjects
    public List<SubjectDTO> findsubjectByName(String name){
        String url = STUDENT_SERVICE_URL + "/subjects/" + name;
        return restTemplate.getForObject(url, List.class);
    }

}

