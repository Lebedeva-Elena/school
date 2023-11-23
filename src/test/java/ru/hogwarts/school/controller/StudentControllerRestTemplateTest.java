package ru.hogwarts.school.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Student;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class StudentControllerRestTemplateTest {
    @Autowired
    TestRestTemplate restTemplate;
    @LocalServerPort
    int port;
    String baseUrl;
    Student student = new Student(1L, "Ron", 13);
    @BeforeEach
    void beforeEach(){
       baseUrl = "http//localhost:" + port + "/student";

    }

    @Test
    void create_shouldReturnStudentAndStatus200() {
        ResponseEntity<Student> result = restTemplate.postForEntity(
                baseUrl,
                student,
                Student.class
        );
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(student, result.getBody());
    }
}
