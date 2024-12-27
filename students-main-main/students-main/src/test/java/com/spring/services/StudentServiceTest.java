package com.spring.services;
import com.spring.entities.Students;
import com.spring.entities.Subject;
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

@ExtendWith(MockitoExtension.class) // Add this line to enable Mockito in JUnit 5
public class StudentServiceTest {
    @Mock
    private StudentRepo studentRepo;  // Mock the StudentRepo
    @InjectMocks
    private StudentService studentService;  // Inject the mock into the service
    @Test
    public void testAddStudentservice() {
        Students student = new Students();
        student.setId(1);
        student.setName("John Doe");
        student.setAge(20);
        student.setGender("Male");
        student.setDob("2004-01-01");
        student.setCourse("Computer Science");
        student.setCourseStartYear(2022);
        student.setCourseEndYear(2026);

        // Create subjects for the student
        Subject subject1 = new Subject();
        subject1.setSubId(1);
        subject1.setName("Mathematics");
        subject1.setMarks(95);
        Subject subject2 = new Subject();
        subject2.setSubId(2);
        subject2.setName("Physics");
        subject2.setMarks(88);

        // Add subjects to the student
        student.setSubjects(Arrays.asList(subject1, subject2));

        // Mock the repository save method
        when(studentRepo.save(any(Students.class))).thenReturn(student);

        // Act: Call the addStudent method
        Students savedStudent = studentService.addStudent(student);

        verify(studentRepo, times(1)).save(student);  // Ensure the save method is called once
        assertNotNull(savedStudent);
        assertEquals("John Doe", savedStudent.getName());
        assertEquals(20, savedStudent.getAge());
        assertEquals("Computer Science", savedStudent.getCourse());
        assertEquals(2022, savedStudent.getCourseStartYear());
        assertEquals(2026, savedStudent.getCourseEndYear());

        assertNotNull(savedStudent.getSubjects());  // Verify subjects are not null
        assertEquals(2, savedStudent.getSubjects().size());  // Verify that there are two subjects
        assertEquals("Mathematics", savedStudent.getSubjects().get(0).getName());  // Verify the first subject
        assertEquals(95, savedStudent.getSubjects().get(0).getMarks());  // Verify marks for the first subject
        assertEquals("Physics", savedStudent.getSubjects().get(1).getName());  // Verify the second subject
        assertEquals(88, savedStudent.getSubjects().get(1).getMarks());// Verify marks for the second subject
    }

    @Test
    void testUpdateMarks() {
        Subject subject1 = new Subject(1, "Maths", 80);
        Students student1 = new Students(1, "mary", 22, "Female", "13-7-2003", "Computer Science", 2020, 2024, Arrays.asList(subject1));
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

    @Test
    void testdeleteStudent() {
        int studentId = 1;
        Subject subject1 = new Subject(1, "Maths", 80);
        Students student1 = new Students(studentId, "mary", 22, "Female", "13-7-2003", "Computer Science", 2020, 2024, Arrays.asList(subject1));
        studentRepo.deleteById(studentId);
        verify(studentRepo, times(1)).deleteById(studentId);
        assertEquals(studentId,student1.getId());


    }


}

