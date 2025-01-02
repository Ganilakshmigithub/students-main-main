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
    // Happy Path: Test adding a student

    @Test
    public void testConvertoDTO(){
        Subject s1=new Subject(1,"Maths",80);
        Subject s2=new Subject(2,"English",90);
        Students student=new Students(1,"raju",23,"male","23-7-2001","CSE",2019,2023,Arrays.asList(s1,s2));
        StudentDTO studentDTO=studentService.convertToDTO(student);
        assertNotNull(studentDTO);
        assertEquals(student.getId(),studentDTO.getId());
        assertEquals(student.getName(),studentDTO.getName());
        assertEquals(student.getAge(),studentDTO.getAge());
        assertEquals(student.getGender(),studentDTO.getGender());
        assertEquals(student.getDob(),studentDTO.getDob());
        assertEquals(student.getCourse(),studentDTO.getCourse());
        assertEquals(student.getCourseStartYear(),studentDTO.getCourseStartYear());
        assertEquals(student.getCourseEndYear(),studentDTO.getCourseEndYear());
        assertEquals(student.getSubjects().get(0).getSubId(),studentDTO.getSubjects().get(0).getSubId());
        assertEquals(student.getSubjects().get(1).getSubId(),studentDTO.getSubjects().get(1).getSubId());
        assertEquals(student.getSubjects().get(0).getName(),studentDTO.getSubjects().get(0).getName());
        assertEquals(student.getSubjects().get(1).getName(),studentDTO.getSubjects().get(1).getName());
        assertEquals(student.getSubjects().get(0).getMarks(),studentDTO.getSubjects().get(0).getMarks());
        assertEquals(student.getSubjects().get(1).getMarks(),studentDTO.getSubjects().get(1).getMarks());

    }
    @Test
    public void testAddStudentSuccess() {
        StudentDTO student = new StudentDTO();
        student.setId(1);
        student.setName("John Doe");
        student.setAge(20);
        student.setGender("Male");
        student.setDob("2004-01-01");
        student.setCourse("Computer Science");
        student.setCourseStartYear(2022);
        student.setCourseEndYear(2026);
        SubjectDTO subject1 = new SubjectDTO(1, "Mathematics", 95);
        SubjectDTO subject2 = new SubjectDTO(2, "Physics", 88);
        student.setSubjects(Arrays.asList(subject1, subject2));
        when(studentRepo.save(any())).thenReturn(student);
        StudentDTO savedStudent = studentService.addStudent(student);
        verify(studentRepo, times(1)).save(any());
        assertNotNull(savedStudent);
        assertEquals("John Doe", savedStudent.getName());
        assertEquals(20, savedStudent.getAge());
        assertEquals("Computer Science", savedStudent.getCourse());
        assertEquals(2022, savedStudent.getCourseStartYear());
        assertEquals(2026, savedStudent.getCourseEndYear());
        assertNotNull(savedStudent.getSubjects());
        assertEquals(2, savedStudent.getSubjects().size());
        assertEquals("Mathematics", savedStudent.getSubjects().get(0).getName());
        assertEquals(95, savedStudent.getSubjects().get(0).getMarks());
        assertEquals("Physics", savedStudent.getSubjects().get(1).getName());
        assertEquals(88, savedStudent.getSubjects().get(1).getMarks());
    }
    // Happy Path: Test updating marks of a student
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
    // Test when student is not found while updating marks (failure case)
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
        // Verify no changes in marks since subject was not found
        assertNotEquals(newMarks, subject1.getMarks());
    }
    // Test updating marks when the student has no subjects
    @Test
    void testUpdateMarksNoSubjects() {
        Students student1 = new Students(1, "John", 20, "Male", "01-01-2003", "Physics", 2022, 2026, Arrays.asList());
        int studentId = 1;
        int subjectId = 1;
        int newMarks = 95;
        when(studentRepo.findById(studentId)).thenReturn(Optional.of(student1));
        assertThrows(StudentNotFoundException.class, () -> {
            studentService.updateMarks(studentId, subjectId, newMarks);
        });
    }
    // Test successful deletion of a student
    @Test
    void testDeleteStudentSuccess() {
        int studentId = 1;
        when(studentRepo.existsById(studentId)).thenReturn(true);
        doNothing().when(studentRepo).deleteById(studentId); //should not perfrom anything
        studentService.deleteStudentById(studentId);
        verify(studentRepo, times(1)).deleteById(studentId);
    }
    // Test when student is not found while deleting (failure case)
    @Test
    void testDeleteStudentNotFound() {
        int studentId = 999;
        when(studentRepo.existsById(studentId)).thenReturn(false);
        RuntimeException exception = assertThrows(StudentNotFoundException.class, () -> studentService.deleteStudentById(studentId));
        assertEquals("Student with id 999 not found", exception.getMessage());
    }
}






