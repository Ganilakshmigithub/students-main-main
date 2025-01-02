package com.spring.dtos;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;
public class StudentDTO {
    private int id;
    @NotNull(message = "name should not be null")
    private String name;
    @Min(value = 18,message = "age should not be less than 18")
    private int age;
    private String gender;
    @Max(value = 31-12-1995,message = "age must be after 95 born")
    private String dob;
    private String course;
    private int courseStartYear;
    private int courseEndYear;
    private List<SubjectDTO> subjects;
    // Constructor


    public StudentDTO() {
    }

    public StudentDTO(int id, String name, int age, String gender, String dob, String course, int courseStartYear, int courseEndYear, List<SubjectDTO> subjects) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.dob = dob;
        this.course = course;
        this.courseStartYear = courseStartYear;
        this.courseEndYear = courseEndYear;
        this.subjects = subjects;
    }
    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getDob() {
        return dob;
    }
    public void setDob(String dob) {
        this.dob = dob;
    }
    public String getCourse() {
        return course;
    }
    public void setCourse(String course) {
        this.course = course;
    }
    public int getCourseStartYear() {
        return courseStartYear;
    }
    public void setCourseStartYear(int courseStartYear) {
        this.courseStartYear = courseStartYear;
    }
    public int getCourseEndYear() {
        return courseEndYear;
    }
    public void setCourseEndYear(int courseEndYear) {
        this.courseEndYear = courseEndYear;
    }
    public List<SubjectDTO> getSubjects() {
        return subjects;
    }
    public void setSubjects(List<SubjectDTO> subjects) {
        this.subjects = subjects;
    }
}

