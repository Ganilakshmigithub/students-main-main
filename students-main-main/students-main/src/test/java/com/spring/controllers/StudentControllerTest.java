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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
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
public void testAddStudentSuccess() throws Exception {
    // Prepare subject DTO with subId, name, and marks
    SubjectDTO subject1 = new SubjectDTO(1, "Maths", 80);
    SubjectDTO subject2 = new SubjectDTO(2, "English", 90);
    // Prepare a valid student DTO
    StudentDTO studentDTO = new StudentDTO(1, "pooja", 24, "female", "7-9-2000", "cse", 2018, 2022, Arrays.asList(subject1, subject2));
    // Mock the service method to return the studentDTO when addStudent is called
    when(studentService.addStudent(Mockito.any(StudentDTO.class))).thenReturn(studentDTO);
    // Perform POST request and check assertions
    mockMvc.perform(MockMvcRequestBuilders.post("/students/save")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"id\":1,\"name\":\"pooja\",\"age\":24,\"gender\":\"female\",\"dob\":\"7-9-2000\",\"course\":\"cse\",\"courseStartYear\":2018,\"courseEndYear\":2022,\"subjects\":[{\"subId\":1,\"name\":\"Maths\",\"marks\":80},{\"subId\":2,\"name\":\"English\",\"marks\":90}]}"))
            .andExpect(status().isCreated()) // Expect 201 Created
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json("{\"id\":1,\"name\":\"pooja\",\"age\":24,\"gender\":\"female\",\"dob\":\"7-9-2000\",\"course\":\"cse\",\"courseStartYear\":2018,\"courseEndYear\":2022,\"subjects\":[{\"subId\":1,\"name\":\"Maths\",\"marks\":80},{\"subId\":2,\"name\":\"English\",\"marks\":90}]}"));
}
    @Test
    public void testAddStudentValidationFailure() throws Exception {
        // Prepare a valid student DTO with invalid age (e.g., age < 18)
        SubjectDTO subject1 = new SubjectDTO(1, "Maths", 80);
        SubjectDTO subject2 = new SubjectDTO(2, "English", 90);
        // Invalid student DTO (age = 10 should fail validation)
        String studentJson = "{\"id\":1,\"name\":\"pooja\",\"age\":10,\"gender\":\"female\",\"dob\":\"7-9-2000\",\"course\":\"cse\",\"courseStartYear\":2018,\"courseEndYear\":2022,\"subjects\":[{\"subId\":1,\"name\":\"Maths\",\"marks\":80},{\"subId\":2,\"name\":\"English\",\"marks\":90}]}";
        // Perform POST request and check assertions
        mockMvc.perform(MockMvcRequestBuilders.post("/students/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andExpect(status().isBadRequest()) // Expect 400 for validation failure
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"message\":\"Validation Failed..Please Check Data Before Posting\"}"));
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

