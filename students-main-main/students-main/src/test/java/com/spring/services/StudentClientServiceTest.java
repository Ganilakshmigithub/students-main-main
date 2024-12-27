package com.spring.services;
import com.spring.dtos.StudentDTO;
import com.spring.dtos.SubjectDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
public class StudentClientServiceTest {
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private StudentClientService studentClientService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    // A Helper method to create StudentDTO with SubjectDTOs
    private StudentDTO createStudent(int id, String name, int age, String gender, String dob, String course,
                                     int courseStartYear, int courseEndYear, SubjectDTO... subjects) {
        return new StudentDTO(id, name, age, gender, dob, course, courseStartYear, courseEndYear, Arrays.asList(subjects));
    }
    @Test
    void testGetStudentByName() {
        String name = "raju";
        SubjectDTO subject1 = new SubjectDTO(1, "Mathematics", 90);
        SubjectDTO subject2 = new SubjectDTO(2, "Physics", 85);
        // Create StudentDTO objects with subjects using the helper method
        StudentDTO student1 = createStudent(1, "raju", 25, "Male", "1999-05-15", "Computer Science", 2020, 2024, subject1, subject2);
        StudentDTO student2 = createStudent(2, "raju", 26, "Male", "1998-08-22", "Mechanical Engineering", 2019, 2023, subject1);
        // Mock the RestTemplate behavior
        when(restTemplate.getForObject("http://localhost:8083/students/name/" + name, List.class)).thenReturn(Arrays.asList(student1, student2));
        // Act: Call the method under test
        List<StudentDTO> result = studentClientService.getStudentByName(name);
        // Assert: Verify the result
        assertEquals(2, result.size());
        // Verify student 1 details
        assertStudentDetails(result.get(0), 1, "raju", 25, "Male", "1999-05-15", "Computer Science", 2020, 2024, subject1, subject2);
        // Verify student 2 details
        assertStudentDetails(result.get(1), 2, "raju", 26, "Male", "1998-08-22", "Mechanical Engineering", 2019, 2023, subject1);
        // Verify that the restTemplate's getForObject method was called once with the correct URL
        verify(restTemplate, times(1)).getForObject("http://localhost:8083/students/name/" + name, List.class);
    }
    @Test
    void testGetStudentByAge() {
        // Arrange: create mock response with students
        int age = 25;
        SubjectDTO subject1 = new SubjectDTO(1, "Mathematics", 90);
        SubjectDTO subject2 = new SubjectDTO(2, "Physics", 85);
        // Create StudentDTO objects with subjects using the helper method
        StudentDTO student1 = createStudent(1, "John", 25, "Male", "1999-05-15", "Computer Science", 2020, 2024, subject1, subject2);
        StudentDTO student2 = createStudent(2, "Doe", 25, "Male", "1998-08-22", "Mechanical Engineering", 2019, 2023, subject1);
        // Mock the RestTemplate behavior
        when(restTemplate.getForObject("http://localhost:8083/students/age/" + age, List.class)).thenReturn(Arrays.asList(student1, student2));
        List<StudentDTO> result = studentClientService.getStudentByAge(age);
        assertEquals(2, result.size());

        // Verify student 1 details
        assertStudentDetails(result.get(0), 1, "John", 25, "Male", "1999-05-15", "Computer Science", 2020, 2024, subject1, subject2);

        // Verify student 2 details
        assertStudentDetails(result.get(1), 2, "Doe", 25, "Male", "1998-08-22", "Mechanical Engineering", 2019, 2023, subject1);
        // Verify that the restTemplate's getForObject method was called once with the correct URL
        verify(restTemplate, times(1)).getForObject("http://localhost:8083/students/age/" + age, List.class);
    }

    @Test
    void testgetAllStudents() {
        SubjectDTO subject1 = new SubjectDTO(1, "Mathematics", 90);
        SubjectDTO subject2 = new SubjectDTO(2, "Physics", 85);
        // Create StudentDTO objects with subjects using the helper method
        StudentDTO student1 = createStudent(1, "John", 25, "Male", "1999-05-15", "Computer Science", 2020, 2024, subject1, subject2);
        StudentDTO student2 = createStudent(2, "vijay", 26, "Male", "1998-08-22", "Mechanical Engineering", 2019, 2023, subject1);
        // Mock the RestTemplate behavior
        when(restTemplate.getForObject("http://localhost:8083/students/all?page=0&size=2" , List.class)).thenReturn(Arrays.asList(student1, student2));
        List<StudentDTO> result = studentClientService.getAllStudents();
        assertEquals(2, result.size());

        // Verify student 1 details
        assertStudentDetails(result.get(0), 1, "John", 25, "Male", "1999-05-15", "Computer Science", 2020, 2024, subject1, subject2);

        // Verify student 2 details
        assertStudentDetails(result.get(1), 2, "vijay", 26, "Male", "1998-08-22", "Mechanical Engineering", 2019, 2023, subject1);

        // Verify that the restTemplate's getForObject method was called once with the correct URL
        verify(restTemplate, times(1)).getForObject("http://localhost:8083/students/all?page=0&size=2", List.class);
    }

    @Test
    void testfindSubjectByname(){

        String subject = "Mathematics";
        SubjectDTO subject1 = new SubjectDTO(1, "Mathematics", 90);
        when(restTemplate.getForObject("http://localhost:8083/students/subjects/"+subject,List.class)).thenReturn(Arrays.asList(subject1));
        List<SubjectDTO> result = studentClientService.findsubjectByName(subject);

        assertEquals(1, result.size());
        assertEquals(1,result.get(0).getSubId());
        assertEquals("Mathematics",result.get(0).getName());
        assertEquals(90,result.get(0).getMarks());

      verify(restTemplate,times(1)).getForObject("http://localhost:8083/students/subjects/"+subject,List.class);

    }
    // Helper method to assert student details and their subjects
    private void assertStudentDetails(StudentDTO student, int expectedId, String expectedName, int expectedAge,
                                      String expectedGender, String expectedDob, String expectedCourse,
                                      int expectedCourseStartYear, int expectedCourseEndYear, SubjectDTO... expectedSubjects) {
        assertEquals(expectedId, student.getId());
        assertEquals(expectedName, student.getName());
        assertEquals(expectedAge, student.getAge());
        assertEquals(expectedGender, student.getGender());
        assertEquals(expectedDob, student.getDob());
        assertEquals(expectedCourse, student.getCourse());
        assertEquals(expectedCourseStartYear, student.getCourseStartYear());
        assertEquals(expectedCourseEndYear, student.getCourseEndYear());
        assertEquals(expectedSubjects.length, student.getSubjects().size());
        for (int i = 0; i < expectedSubjects.length; i++) {
            assertEquals(expectedSubjects[i].getSubId(), student.getSubjects().get(i).getSubId());
            assertEquals(expectedSubjects[i].getName(), student.getSubjects().get(i).getName());
            assertEquals(expectedSubjects[i].getMarks(), student.getSubjects().get(i).getMarks());
        }
    }
}

