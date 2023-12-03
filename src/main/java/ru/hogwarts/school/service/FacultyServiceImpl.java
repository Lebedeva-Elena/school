package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.*;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository faculties) {
        this.facultyRepository = faculties;
    }

    @Override
    public Faculty create(Faculty faculty) {
        return facultyRepository.save(faculty);

    }

    @Override
    public Faculty read(long id) {
        return facultyRepository.findById(id).
                orElseThrow(() -> new FacultyNotFoundException("Факультет с id " + id +
                        " не найден в списке"));

    }

    @Override
    public Faculty update(Faculty faculty) {
        Faculty facultyDB = read(faculty.getId());
        facultyDB.setName(faculty.getName());
        facultyDB.setColor(faculty.getColor());

        return facultyRepository.save(facultyDB);
    }

    @Override
    public Faculty delete(long id) {
        Faculty faculty = read(id);
        facultyRepository.delete(faculty);
        return faculty;
    }

    @Override
    public Collection<Faculty> readByColor(String color) {
        return facultyRepository.findAllByColor(color);
    }

    @Override
    public Collection<Faculty> readByNameContainingIgnoreCaseOrColorContainingIgnoreCase(String name, String color) {
        return facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(name, color);
    }
}


