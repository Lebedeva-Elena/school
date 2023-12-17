package ru.hogwarts.school.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
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
        read(faculty.getId());
        return facultyRepository.save(faculty);
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

    @Override
    public ResponseEntity<String> getMustLongNameFaculty() {
        Optional<String> maxFacultyName = facultyRepository
                .findAll()
                .stream()
                .map(Faculty::getName)
                .max(Comparator.comparing(String::length));
        if (maxFacultyName.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(maxFacultyName.get());

        }
    }
}




