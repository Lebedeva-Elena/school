package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.*;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository faculties;

    public FacultyServiceImpl(FacultyRepository faculties) {
        this.faculties = faculties;
    }

    @Override
    public Faculty create(Faculty faculty) {
        return faculties.save(faculty);

    }

    @Override
    public Faculty read(long id) {
        return faculties.findById(id).
                orElseThrow(() -> new FacultyNotFoundException("Факультет с id " + id +
                        " не найден в списке"));

    }

    @Override
    public Faculty update(Faculty faculty) {
        read(faculty.getId());
        return faculties.save(faculty);
    }

    @Override
    public Faculty delete(long id) {
        Faculty faculty = read(id);
        faculties.delete(faculty);
        return faculty;
    }

    @Override
    public Collection<Faculty> readByColor(String color) {
        return faculties.findAllByColor(color);
    }

    @Override
    public Collection<Faculty> readByNameContainingIgnoreCaseOrColorContainingIgnoreCase(String name, String color) {
        return faculties.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(name, color);
    }
}


