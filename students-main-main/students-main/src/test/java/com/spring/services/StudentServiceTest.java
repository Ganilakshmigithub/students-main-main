package com.spring.services;
import com.spring.dtos.StudentDTO;
import com.spring.dtos.SubjectDTO;
import com.spring.entities.Students;
import com.spring.entities.Subject;
import com.spring.exceptions.StudentNotFoundException;
import com.spring.repos.StudentRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.Optional;
@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {
    @Mock
    private StudentRepo studentRepo;
    @InjectMocks
    private StudentService studentService;
    // Test converting from Students entity to StudentDTO (cover edge cases)
    @Test
    public void testConvertToDTO() {
        Subject s1 = new Subject(1, "Maths", 80);
        Subject s2 = new Subject(2, "English", 90);
        Students student = new Students(1, "raju", 23, "male", "23-7-2001", "CSE", 2019, 2023, Arrays.asList(s1, s2));
        // Normal case with valid student
        StudentDTO studentDTO = studentService.convertToDTO(student);
        assertNotNull(studentDTO);
        assertEquals(student.getId(), studentDTO.getId());
        // More assertions...
        // Case with empty subjects list
        student = new Students(2, "John", 25, "male", "01-01-1998", "ECE", 2020, 2024, Arrays.asList());
        studentDTO = studentService.convertToDTO(student);
        assertNotNull(studentDTO);
        assertTrue(studentDTO.getSubjects().isEmpty(), "Subjects should be empty");
    }
    // Test converting from StudentDTO to Students entity (cover edge cases)
    @Test
    public void testConvertToEntity() {
        SubjectDTO s1 = new SubjectDTO(1, "Physics", 80);
        StudentDTO student = new StudentDTO(1, "Bhavya", 23, "Female", "5-7-2001", "ECE", 2019, 2023, Arrays.asList(s1));
        Students students = studentService.convertToEntity(student);
        assertNotNull(students);
        assertEquals(student.getId(), students.getId());
        // More assertions...
        // Case with empty subject list
        student = new StudentDTO(2, "Alex", 26, "Male", "10-10-1997", "BBA", 2021, 2025, Arrays.asList());
        students = studentService.convertToEntity(student);
        assertNotNull(students);
        assertTrue(students.getSubjects().isEmpty(), "Subjects should be empty");
    }
    // Test adding a student successfully
    @Test
    public void testAddStudentSuccess() {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(1);
        studentDTO.setName("John Doe");
        studentDTO.setAge(20);
        studentDTO.setGender("Male");
        studentDTO.setDob("2004-01-01");
        studentDTO.setCourse("Computer Science");
        studentDTO.setCourseStartYear(2022);
        studentDTO.setCourseEndYear(2026);
        SubjectDTO subject1 = new SubjectDTO(1, "Mathematics", 95);
        SubjectDTO subject2 = new SubjectDTO(2, "Physics", 88);
        studentDTO.setSubjects(Arrays.asList(subject1, subject2));
        // Mock the behavior of studentRepo.save() and convertToDTO
        when(studentRepo.save(any(Students.class))).thenReturn(studentService.convertToEntity(studentDTO));
        // Call the method
        StudentDTO result = studentService.addStudent(studentDTO);
        // Verifications
        verify(studentRepo, times(1)).save(any(Students.class));
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals(20, result.getAge());
        assertEquals("Computer Science", result.getCourse());
        assertEquals(2022, result.getCourseStartYear());
        assertEquals(2026, result.getCourseEndYear());
        assertNotNull(result.getSubjects());
        assertEquals(2, result.getSubjects().size());
        assertEquals("Mathematics", result.getSubjects().get(0).getName());
        assertEquals(95, result.getSubjects().get(0).getMarks());
    }
    // Test updating marks of a student successfully
    @Test
    void testUpdateMarksSuccess() {
        Subject subject1 = new Subject(1, "Maths", 80);
        Students student1 = new Students(1, "Mary", 22, "Female", "13-7-2003", "Computer Science", 2020, 2024, Arrays.asList(subject1));
        int studentId = 1;
        int subjectId = 1;
        int newMarks = 95;
        when(studentRepo.findById(studentId)).thenReturn(Optional.of(student1));
        studentService.updateMarks(studentId, subjectId, newMarks);
        verify(studentRepo, times(1)).save(argThat(updatedStudent ->
                updatedStudent.getSubjects().stream()
                        .anyMatch(subject -> subject.getSubId() == subjectId && subject.getMarks() == newMarks)
        ));
        assertEquals(newMarks, subject1.getMarks());
    }
    // Test when student is not found while updating marks
    @Test
    void testUpdateMarksStudentNotFound() {
        int studentId = 999;
        int subjectId = 1;
        int newMarks = 95;
        when(studentRepo.findById(studentId)).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, () -> {
            studentService.updateMarks(studentId, subjectId, newMarks);
        });
        verify(studentRepo, never()).save(any());
    }
    // Test when the subject doesn't exist in student's subjects while updating marks
    @Test
    void testUpdateMarksSubjectNotFound() {
        Subject subject1 = new Subject(1, "Maths", 80);
        Students student1 = new Students(1, "Mary", 22, "Female", "13-7-2003", "Computer Science", 2020, 2024, Arrays.asList(subject1));
        int studentId = 1;
        int subjectId = 999;  // Non-existent subject ID
        int newMarks = 95;
        when(studentRepo.findById(studentId)).thenReturn(Optional.of(student1));
        assertThrows(StudentNotFoundException.class, () -> {
            studentService.updateMarks(studentId, subjectId, newMarks);
        });
        assertNotEquals(newMarks, subject1.getMarks());
    }
    // Test successful deletion of a student
    @Test
    void testDeleteStudentSuccess() {
        int studentId = 1;
        when(studentRepo.existsById(studentId)).thenReturn(true);
        doNothing().when(studentRepo).deleteById(studentId);
        studentService.deleteStudentById(studentId);
        verify(studentRepo, times(1)).deleteById(studentId);
    }
    // Test when student is not found while deleting
    @Test
    void testDeleteStudentNotFound() {
        int studentId = 999;
        when(studentRepo.existsById(studentId)).thenReturn(false);
        RuntimeException exception = assertThrows(StudentNotFoundException.class, () -> studentService.deleteStudentById(studentId));
        assertEquals("Student with id 999 not found", exception.getMessage());
        verify(studentRepo, never()).deleteById(studentId);
    }
    // Test when updateMarks is called on a student with null subjects
    @Test
    void testUpdateMarksSubjectsNull() {
        Students student1 = new Students(1, "John", 20, "Male", "01-01-2003", "Physics", 2022, 2026, null);
        int studentId = 1;
        int subjectId = 1;
        int newMarks = 95;
        when(studentRepo.findById(studentId)).thenReturn(Optional.of(student1));
        assertThrows(NullPointerException.class, () -> {
            studentService.updateMarks(studentId, subjectId, newMarks);
        });
    }
}