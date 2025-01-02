package com.spring.controllers;
import com.spring.dtos.StudentDTO;
import com.spring.dtos.SubjectDTO;
import com.spring.entities.Students;
import com.spring.entities.Subject;
import com.spring.services.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StudentControllerTest {
    private MockMvc mockMvc; // Manually set up MockMvc
    @InjectMocks
    private StudentController studentController; // Controller with mocked dependencies
    private final StudentService studentService = Mockito.mock(StudentService.class); // Manual mock
    private Students student;

    @BeforeEach
    public void setUp() {
        // Initialize mocks and MockMvc
        MockitoAnnotations.openMocks(this); //intializing mocks
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
        // Creating Subjects
        Subject s1 = new Subject(1, "Maths", 80);
        Subject s2 = new Subject(2, "English", 90);

        // Creating a student
        student = new Students(1, "Abhi", 20, "Male", "22-3-2004", "computer science", 2020, 2024, Arrays.asList(s1, s2));
    }
//
     //Test for adding a student
    @Test
    public void testAddStudent() throws Exception {
        SubjectDTO s3 = new SubjectDTO(1, "Maths", 80);
        SubjectDTO s4 = new SubjectDTO(2, "English", 90);
        StudentDTO s = new StudentDTO(1, "Abhi", 20, "Male", "22-3-2004", "computer science", 2020, 2024, Arrays.asList(s3, s4));
        when(studentService.addStudent(Mockito.any(StudentDTO.class))).thenReturn(s);
        mockMvc.perform(post("/students/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                               "id": 1,
                              "name":"Abhi",
                              "age":20,
                              "gender":"male",
                              "dob":"22-3-2004",
                              "course":"computer science",
                              "courseStartYear":2020,
                              "courseEndYear":2024,
                              "subjects":[
                                {"subId":1,"name":"Maths","marks":80},
                                {"subId":2,"name":"English","marks":90}
                              ]
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                    {
                      "id":1,
                      "name":"Abhi",
                      "age":20,
                      "gender":"Male",
                      "dob":"22-3-2004",
                      "course":"computer science",
                      "courseStartYear":2020,
                      "courseEndYear":2024,
                      "subjects":[
                        {"subId":1,"name":"Maths","marks":80},
                        {"subId":2,"name":"English","marks":90}
                      ]
                    }
                    """));
    }
     //Test for updating marks
    @Test
    public void testUpdateMarksControl() throws Exception {
        int studentId = 1;
        int subjectId = 1;
        int newMarks = 70;
        // Mock the service behavior
        doNothing().when(studentService).updateMarks(studentId, subjectId, newMarks);
        mockMvc.perform(put("/students/update/{student_id}/{subject_id}/newmarks", studentId, subjectId)
                        .param("newmarks", String.valueOf(newMarks)))
                .andExpect(status().isOk())
                .andExpect(content().string("marks updated successfully..!!"));
    }

    @Test
    public void testDeleteStudent() throws Exception {
        int id=1;
        doNothing().when(studentService).deleteStudentById(id);
        mockMvc.perform(delete("/students/del/{id}", id))
                .andExpect(status().isOk());
    }
}

