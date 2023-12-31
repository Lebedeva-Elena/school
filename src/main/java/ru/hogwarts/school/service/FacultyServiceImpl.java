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
    private final Logger logger = LoggerFactory.getLogger(FacultyServiceImpl.class);
    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Faculty create(Faculty faculty) {
        logger.info("Was invoked method for create faculty");
        return facultyRepository.save(faculty);

    }

    @Override
    public Faculty read(long id) {
        logger.info("Was invoked method for read faculty");
        logger.error("There is not faculty with id = " + id);
        return facultyRepository.findById(id).
                orElseThrow(() -> new FacultyNotFoundException("Факультет с id " + id +
                        " не найден в списке"));

    }

    @Override
    public Faculty update(Faculty faculty) {
        read(faculty.getId());
        logger.info("Was invoked method for update faculty");
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty delete(long id) {
        Faculty faculty = read(id);
        logger.info("Was invoked method for delete faculty {}", id);
        facultyRepository.delete(faculty);
        return faculty;
    }

    @Override
    public Collection<Faculty> readByColor(String color) {
        logger.info("Was invoked method for readByColor faculty {}", color);
        return facultyRepository.findAllByColor(color);
    }

    @Override
    public Collection<Faculty> readByNameContainingIgnoreCaseOrColorContainingIgnoreCase(String name, String color) {
        logger.info("Was invoked method for readByNameContainingIgnoreCaseOrColorContainingIgnoreCase faculty");
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




