package ru.hogwarts.school.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.List;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class StudentControllerRestTemplateTest {
    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    StudentRepository studentRepository;
    @LocalServerPort
    int port;
    String baseUrl;
    Student student = new Student(1L, "Ron", 13);

    @BeforeEach
    void beforeEach() {
        baseUrl = "http://localhost:" + port + "/student";
        studentRepository.deleteAll();

    }

    @Test
    void create_shouldReturnStudentAndStatus200() {
        ResponseEntity<Student> result = restTemplate.postForEntity(
                baseUrl,
                student,
                Student.class
        );
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        Student newStudent = result.getBody();

        assertThat(newStudent.getName()).isEqualTo(student.getName());
        assertThat(newStudent.getAge()).isEqualTo(student.getAge());
    }

    @Test
    void read_shouldReturnStatus404() {
        ResponseEntity<String> result = restTemplate.getForEntity(
                baseUrl + "/" + student.getId(),
                String.class
        );
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Студент с id " + student.getId() + " не найден в списке",
                result.getBody());

    }

    @Test
    void update_shouldReturnStudentAndStatus200() {
        Student saveStudent = studentRepository.save(student);
        ResponseEntity<Student> result = restTemplate.exchange(
                baseUrl,
                HttpMethod.PUT,
                new HttpEntity<>(saveStudent),
                Student.class
        );
        assertEquals(result.getStatusCode(), HttpStatus.OK);
        assertEquals(saveStudent, result.getBody());

    }

    @Test
    void delete_shouldReturnStatus404() {
        ResponseEntity<String> result = restTemplate.exchange(
                baseUrl + "/" + student.getId(),
                HttpMethod.DELETE,
                new HttpEntity<>(student),
                String.class
        );
        assertEquals(result.getStatusCode(), HttpStatus.NOT_FOUND);
        assertEquals("Студент с id " + student.getId() + " не найден в списке",
                result.getBody());
    }

    @Test
    void readByAge_shouldReturnCollectionStudentsSameAgeAndStatus200() {
        Student student1 = new Student(2L, "Harry", 13);
        Student saveStudent = studentRepository.save(student);
        Student saveStudent1 = studentRepository.save(student1);

        ResponseEntity<List<Student>> result = restTemplate.exchange(
                baseUrl + "?age=" + student.getAge(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }

        );
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(List.of(saveStudent, saveStudent1), result.getBody());
    }

    @Test
    void getByAgeBetween_shouldReturnCollectionStudentsFromMinimumAgeToMaximumAndStatus200() {
        Student student1 = new Student(2L, "Harry", 8);
        Student student2 = new Student(3L, "Hermiona", 10);
        Student student3 = new Student(4L, "Malfoy", 15);
        Student saveStudent = studentRepository.save(student);
        Student saveStudent1 = studentRepository.save(student1);
        Student saveStudent2 = studentRepository.save(student2);
        Student saveStudent3 = studentRepository.save(student3);

        ResponseEntity<List<Student>> result = restTemplate.exchange(
                baseUrl + "/age?minAge= " + saveStudent2.getAge() + " &maxAge= " + saveStudent3.getAge(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }

        );

        assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        assertThat(List.of(saveStudent, saveStudent2, saveStudent3)).isEqualTo(result.getBody());

    }

}
