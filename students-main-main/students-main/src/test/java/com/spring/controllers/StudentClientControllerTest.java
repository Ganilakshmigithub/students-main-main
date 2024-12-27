package com.spring.controllers;
import com.spring.dtos.StudentDTO;
import com.spring.dtos.SubjectDTO;
import com.spring.services.StudentClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ExtendWith(MockitoExtension.class)
public class StudentClientControllerTest {
    @Mock
    private StudentClientService studentClientService;  // Mock the service layer
    @InjectMocks
    private StudentClientController studentClientController;  // Inject the mock service into the controller
    private MockMvc mockMvc;  // MockMvc to simulate HTTP requests and test controller
    private StudentDTO student;
    private SubjectDTO subject1;
    private SubjectDTO subject2;
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentClientController).build(); // Setup MockMvc
        // Prepare test data for subjects
        subject1 = new SubjectDTO(1, "Maths", 90);
        subject2 = new SubjectDTO(2, "Physics", 80);
        // Create a StudentDTO with sample data
        student = new StudentDTO(1, "Raju", 22, "Male", "2001-01-01", "Computer Science", 2020, 2024, Arrays.asList(subject1, subject2));
    }
    @Test
    void testGetStudentByNameClient() throws Exception {
        String name = "Raju";
        when(studentClientService.getStudentByName(name)).thenReturn(Arrays.asList(student));
        mockMvc.perform(MockMvcRequestBuilders.get("/students/name/{name}", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Raju"))
                .andExpect(jsonPath("$[0].age").value(22))
                .andExpect(jsonPath("$[0].gender").value("Male"))
                .andExpect(jsonPath("$[0].dob").value("2001-01-01"))
                .andExpect(jsonPath("$[0].course").value("Computer Science"))
                .andExpect(jsonPath("$[0].courseStartYear").value(2020))
                .andExpect(jsonPath("$[0].courseEndYear").value(2024))
                .andExpect(jsonPath("$[0].subjects[0].name").value("Maths"))  // Check the subject "Maths"
                .andExpect(jsonPath("$[0].subjects[0].marks").value(90))  // Check the marks of the subject "Maths"
                .andExpect(jsonPath("$[0].subjects[1].name").value("Physics"))  // Check the subject "Physics"
                .andExpect(jsonPath("$[0].subjects[1].marks").value(80));  // Check the marks of the subject "Physics"
    }

    @Test
    void testgetStudentByAgeClient() throws Exception {
        int age = 22;
        when(studentClientService.getStudentByAge(age)).thenReturn(Arrays.asList(student));
        mockMvc.perform(MockMvcRequestBuilders.get("/students/age/{age}", age))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Raju"))
                .andExpect(jsonPath("$[0].age").value(22))
                .andExpect(jsonPath("$[0].gender").value("Male"))
                .andExpect(jsonPath("$[0].dob").value("2001-01-01"))
                .andExpect(jsonPath("$[0].course").value("Computer Science"))
                .andExpect(jsonPath("$[0].courseStartYear").value(2020))
                .andExpect(jsonPath("$[0].courseEndYear").value(2024))
                .andExpect(jsonPath("$[0].subjects[0].name").value("Maths"))
                .andExpect(jsonPath("$[0].subjects[0].marks").value(90))
                .andExpect(jsonPath("$[0].subjects[1].name").value("Physics"))
                .andExpect(jsonPath("$[0].subjects[1].marks").value(80));



    }
    @Test
    void testgetAllStudentsclient() throws Exception {
        when(studentClientService.getAllStudents()).thenReturn(Arrays.asList(student));
        mockMvc.perform(MockMvcRequestBuilders.get("/students/all?page=0&size=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Raju"))
                .andExpect(jsonPath("$[0].age").value(22))
                .andExpect(jsonPath("$[0].gender").value("Male"))
                .andExpect(jsonPath("$[0].dob").value("2001-01-01"))
                .andExpect(jsonPath("$[0].course").value("Computer Science"))
                .andExpect(jsonPath("$[0].courseStartYear").value(2020))
                .andExpect(jsonPath("$[0].courseEndYear").value(2024))
                .andExpect(jsonPath("$[0].subjects[0].name").value("Maths"))
                .andExpect(jsonPath("$[0].subjects[0].marks").value(90))
                .andExpect(jsonPath("$[0].subjects[1].name").value("Physics"))
                .andExpect(jsonPath("$[0].subjects[1].marks").value(80));


    }
    @Test
    public void testGetSubjectByName() throws Exception {
        String name = "Maths";
        when(studentClientService.findsubjectByName(name)).thenReturn(Arrays.asList(subject1));
        // Then
        mockMvc.perform(MockMvcRequestBuilders.get("/students/subject/{name}", name,List.class))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].subId").value(1))
                .andExpect(jsonPath("$[0].name").value("Maths"))
                .andExpect(jsonPath("$[0].marks").value(90));

    }
}


