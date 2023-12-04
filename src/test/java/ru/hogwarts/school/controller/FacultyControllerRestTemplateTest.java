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
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;

import javax.validation.constraints.Max;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerRestTemplateTest {
    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    FacultyRepository facultyRepository;
    @LocalServerPort
    int port;
    String baseUrl;
    Faculty faculty = new Faculty(1L, "Griffindor", "red");

    @BeforeEach
    void beforeEach() {
        baseUrl = "http://localhost:" + port + "/faculty";
        facultyRepository.deleteAll();
    }

    @Test
    void create_shouldReturnFacultyAndStatus200() {
        ResponseEntity<Faculty> result = restTemplate.postForEntity(
                baseUrl,
                faculty,
                Faculty.class
        );
        assertEquals(result.getStatusCode(), HttpStatus.OK);
        assertEquals(faculty, result.getBody());

    }

    @Test
    void read_shouldReturnStatus404() {
        ResponseEntity<String> result = restTemplate.getForEntity(
                baseUrl + "/" + faculty.getId(),
                String.class
        );
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Факультет с id " + faculty.getId() + " не найден в списке",
                result.getBody());
    }

    @Test
    void update_shouldReturnFacultyAndStatus200() {
        Faculty saveFaculty = facultyRepository.save(faculty);
        ResponseEntity<Faculty> result = restTemplate.exchange(
                baseUrl,
                HttpMethod.PUT,
                new HttpEntity<>(saveFaculty),
                Faculty.class
        );
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(saveFaculty, result.getBody());
    }

    @Test
    void delete_shouldReturnStatus404() {
        ResponseEntity<String> result = restTemplate.exchange(
                baseUrl + "/" + faculty.getId(),
                HttpMethod.DELETE,
                new HttpEntity<>(faculty),
                String.class
        );
        assertEquals(result.getStatusCode(), HttpStatus.NOT_FOUND);
        assertEquals("Факультет с id " + faculty.getId() + " не найден в списке",
                result.getBody());
    }

    @Test
    void readByColor_shouldReturnFacultiesCollectionByColorAndStatus200() {
        Faculty faculty1 = new Faculty(2L, "Slizerin", "red");
        Faculty faculty2 = new Faculty(3L, "Kogtevran", "black");
        Faculty saveFaculty = facultyRepository.save(faculty);
        Faculty saveFaculty1 = facultyRepository.save(faculty1);
        Faculty saveFaculty2 = facultyRepository.save(faculty2);

        ResponseEntity<List<Faculty>> result = restTemplate.exchange(
                baseUrl + "/color?color= " + faculty.getColor(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        assertThat(List.of(saveFaculty, saveFaculty1)).isEqualTo(result.getBody());

    }

//    @Test
//    void readByNameContainingIgnoreCaseOrColorContainingIgnoreCase_shouldReturnCollectionFacultiesAndStatus200() {
//        Faculty faculty1 = new Faculty(1L, "GriFFindor", "RED");
//
//
//        Faculty saveFaculty = facultyRepository.save(faculty);
//        Faculty saveFaculty1 = facultyRepository.save(faculty1);
//
//
//
//        ResponseEntity<List<Faculty>> result = restTemplate.exchange(
//                baseUrl + "/filter?name= " + saveFaculty.getName() + "color= " +
//                        saveFaculty.getColor(),
//                HttpMethod.GET,
//                null,
////                new ParameterizedTypeReference<>()
//
//
//        );
//        assertEquals(HttpStatus.OK,result.getStatusCode());
//
//        assertEquals(result.getBody(),  saveFaculty1==saveFaculty) ;
//    }



}