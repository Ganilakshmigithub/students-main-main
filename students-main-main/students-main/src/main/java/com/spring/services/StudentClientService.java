package com.spring.services;

import com.spring.dtos.CustomPageResponse;
import com.spring.dtos.StudentDTO;
import com.spring.dtos.SubjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
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
    // Fetch all students with pagination
    public CustomPageResponse<StudentDTO> getAllStudents(int page, int size) {
        String url = STUDENT_SERVICE_URL + "/all?page=" + page + "&size=" + size;
        ResponseEntity<CustomPageResponse<StudentDTO>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<CustomPageResponse<StudentDTO>>() {}
        );
        return response.getBody();
    }
    // Fetch subjects by name
    public List<SubjectDTO> findSubjectsByName(String name) {
        String url = STUDENT_SERVICE_URL + "/subjects/" + name;
        return restTemplate.getForObject(url, List.class);
    }
}

