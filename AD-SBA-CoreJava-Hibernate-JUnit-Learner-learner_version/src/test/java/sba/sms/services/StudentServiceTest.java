package sba.sms.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sba.sms.models.Student;
import sba.sms.utils.CommandLine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


class StudentServiceTest {



    private static StudentService studentService;

    @BeforeEach
    void setUp() {
        studentService = new StudentService();
    }

    @AfterEach
    void tearDown() {
        studentService = null;
    }

    @Test
    public void testCreateStudent() throws Exception {
        Student student = new Student();
        student.setEmail("nik@gmail.com");
        student.setName("Nik");
        student.setPassword("pass");

        studentService.createStudent(student);
        assertThat(student.getEmail()).isEqualTo("nik@gmail.com");
    }

    @Test
    public void testGetStudentByEmail() throws Exception {
        String email = "nik@gmail.com";
        Student student = new Student("nik@gmail.com", "Nikh", "pass");
        studentService.getStudentByEmail(email);
        assertThat(student.getEmail()).isEqualTo("nik@gmail.com");
    }



}