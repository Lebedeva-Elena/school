package ru.hogwarts.school.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.FacultyServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
public class FacultyControllerWebMvcTest {
    @MockBean
    FacultyRepository facultyRepository;
    @SpyBean
    FacultyServiceImpl facultyService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    Faculty faculty = new Faculty(1L, "Griffindor", "red");

    @Test
    void create_shouldReturnFacultyAndStatus200() throws Exception {
        when(facultyRepository.save(faculty)).thenReturn(faculty);
        mockMvc.perform(post("/faculty")
                        .content(objectMapper.writeValueAsString(faculty))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(faculty));

    }

    @Test
    void read_shouldReturnStatus404() throws Exception {
        when(facultyRepository.findById(faculty.getId())).thenReturn(Optional.empty());
        mockMvc.perform(get("/faculty/" + faculty.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Факультет с id " + faculty.getId() +
                        " не найден в списке"));
    }


    @Test
    void update_shouldReturnFacultyAndStatus200() throws Exception {
        when(facultyRepository.findById(faculty.getId())).thenReturn(Optional.of(faculty));
        mockMvc.perform(put("/faculty")
                        .content(objectMapper.writeValueAsString(faculty))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(faculty));
    }

    @Test
    void delete_shouldReturnStatus404() throws Exception {
        when(facultyRepository.findById(faculty.getId())).thenReturn(Optional.empty());
        mockMvc.perform(delete("/faculty/" + faculty.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Факультет с id " + faculty.getId() +
                        " не найден в списке"));

    }

    @Test
    void readByColor_shouldReturnFacultiesCollectionAndStatus200() throws Exception {
        Faculty faculty1 = new Faculty(2L, "Mockffindor", "red");

        when(facultyRepository.findAllByColor(faculty.getColor())).thenReturn(List.of(faculty, faculty1));
        mockMvc.perform(get("/faculty/color?color=" + faculty.getColor()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]").value(faculty))
                .andExpect(jsonPath("$.[1]").value(faculty1));

    }

    @Test
    void readByNameContainingIgnoreCaseOrColorContainingIgnoreCase_shouldReturnFacultiesCollectionAndStatus200() throws Exception {
        Faculty faculty1 = new Faculty(2L, "mockffindor", "Red");

        when(facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(faculty.getName(),
                faculty.getColor())).thenReturn(List.of(faculty, faculty1));
        mockMvc.perform(get("/faculty/filter?name=" + faculty.getName() + "&color=" + faculty.getColor()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]").value(faculty))
                .andExpect(jsonPath("$.[1]").value(faculty1));

    }
}