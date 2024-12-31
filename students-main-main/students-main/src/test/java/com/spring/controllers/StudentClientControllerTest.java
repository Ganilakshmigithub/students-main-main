package com.spring.controllers;
import com.spring.dtos.ApiResponse;
import com.spring.dtos.CustomPageResponse;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
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
        when(studentClientService.getStudentsByName(name)).thenReturn(Arrays.asList(student));
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
                .andExpect(jsonPath("$[0].subjects[0].name").value("Maths"))
                .andExpect(jsonPath("$[0].subjects[0].marks").value(90))
                .andExpect(jsonPath("$[0].subjects[1].name").value("Physics"))
                .andExpect(jsonPath("$[0].subjects[1].marks").value(80));
    }
    @Test
    void testGetStudentByNameClientNotFound() throws Exception {
        String name = "NonExistentStudent";
        when(studentClientService.getStudentsByName(name)).thenReturn(Arrays.asList());  // Return empty list
        mockMvc.perform(MockMvcRequestBuilders.get("/students/name/{name}", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())  // Expect 404 status
                .andExpect(jsonPath("$.message").value("Student Not Found with the name"))  // Check message inside ApiResponse
                .andExpect(jsonPath("$.code").value(404));  // Check code inside ApiResponse
    }
    @Test
    void testGetStudentByAgeClient() throws Exception {
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
    void testGetStudentByAgeClientNotFound() throws Exception {
        int age = 999;  // An age that doesn't exist
        when(studentClientService.getStudentByAge(age)).thenReturn(Arrays.asList());  // Return empty list
        mockMvc.perform(MockMvcRequestBuilders.get("/students/age/{age}", age)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())  // Expect 404 status
                .andExpect(jsonPath("$.message").value("Student Not Found with the given age"))  // Check message inside ApiResponse
                .andExpect(jsonPath("$.code").value(404));  // Check code inside ApiResponse
    }
    @Test
    void testGetAllStudentsClient() throws Exception {
        int page = 0;
        int size = 2;
        SubjectDTO subject1 = new SubjectDTO(1, "Mathematics", 90);
        SubjectDTO subject2 = new SubjectDTO(2, "Physics", 85);
        StudentDTO student1 = new StudentDTO(1, "John", 25, "Male", "1999-05-15", "Computer Science", 2020, 2024, Arrays.asList(subject1, subject2));
        StudentDTO student2 = new StudentDTO(2, "Vijay", 26, "Male", "1998-08-22", "Mechanical Engineering", 2019, 2023, Arrays.asList(subject1));
        // Create mock response
        CustomPageResponse<StudentDTO> mockResponse = new CustomPageResponse<>(
                Arrays.asList(student1, student2), 1, 2, 2);
        when(studentClientService.getAllStudents(page, size)).thenReturn(mockResponse);
        mockMvc.perform(MockMvcRequestBuilders.get("/students/all?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("John"))
                .andExpect(jsonPath("$.content[0].age").value(25))
                .andExpect(jsonPath("$.content[0].gender").value("Male"))
                .andExpect(jsonPath("$.content[0].dob").value("1999-05-15"))
                .andExpect(jsonPath("$.content[0].course").value("Computer Science"))
                .andExpect(jsonPath("$.content[0].courseStartYear").value(2020))
                .andExpect(jsonPath("$.content[0].courseEndYear").value(2024))
                .andExpect(jsonPath("$.content[0].subjects[0].name").value("Mathematics"))
                .andExpect(jsonPath("$.content[0].subjects[0].marks").value(90))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].name").value("Vijay"))
                .andExpect(jsonPath("$.content[1].age").value(26))
                .andExpect(jsonPath("$.content[1].gender").value("Male"))
                .andExpect(jsonPath("$.content[1].dob").value("1998-08-22"))
                .andExpect(jsonPath("$.content[1].course").value("Mechanical Engineering"))
                .andExpect(jsonPath("$.content[1].courseStartYear").value(2019))
                .andExpect(jsonPath("$.content[1].courseEndYear").value(2023))
                .andExpect(jsonPath("$.content[1].subjects[0].name").value("Mathematics"))
                .andExpect(jsonPath("$.content[1].subjects[0].marks").value(90))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.totalElements").value(2));
    }
    @Test
    void testGetAllStudentsClientNoStudents() throws Exception {
        int page = 0;
        int size = 2;
        when(studentClientService.getAllStudents(page, size)).thenReturn(new CustomPageResponse<>(Arrays.asList(), 1, 2, 0));  // Empty list of students
        mockMvc.perform(MockMvcRequestBuilders.get("/students/all?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())  // Ensure the content is empty
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.totalElements").value(0));
    }
    @Test
    public void testGetSubjectByName() throws Exception {
        String name = "Maths";
        when(studentClientService.findSubjectsByName(name)).thenReturn(Arrays.asList(subject1));
        mockMvc.perform(MockMvcRequestBuilders.get("/students/subjects/{name}", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].subId").value(1))
                .andExpect(jsonPath("$[0].name").value("Maths"))
                .andExpect(jsonPath("$[0].marks").value(90));
    }
    @Test
    public void testGetSubjectByNameNotFound() throws Exception {
        String name = "NonExistentSubject";
        when(studentClientService.findSubjectsByName(name)).thenReturn(Arrays.asList());  // Return empty list
        mockMvc.perform(MockMvcRequestBuilders.get("/students/subjects/{name}", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())  // Expect 404 status
                .andExpect(jsonPath("$.message").value("Subject Not Found with the name"))  // Check message inside ApiResponse
                .andExpect(jsonPath("$.code").value(404));  // Check code inside ApiResponse
    }
}