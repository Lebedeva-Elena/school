package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final Map<Long, Faculty> faculties = new HashMap<>();

    private long lastId = 0;

    @Override
    public Faculty create(Faculty faculty) {
        if (faculties.containsValue(faculty)) {
            throw new FacultyNotFoundException("Факультет с id " + faculty.getId() + "" +
                    " не найден в списке");
        }
        long id = ++lastId;
        faculty.setId(id);
        return faculties.put(id, faculty);
    }

    @Override
    public Faculty read(long id) {
        Faculty faculty = faculties.get(id);
        if (faculty == null) {
            throw new FacultyNotFoundException("Факультет с id " + id +
                    " не найден в списке");

        }
        return faculty;

    }

    @Override
    public Faculty update(Faculty faculty) {
        if (!faculties.containsKey(faculty.getId())) {
            throw new FacultyNotFoundException("Факультет с id " + faculty.getId() + "" +
                    " не найден в списке");
        }
        return faculties.put(faculty.getId(), faculty);
    }

    @Override
    public Faculty delete(long id) {
        Faculty removedFaculty = faculties.remove(id);
        if (removedFaculty == null) {
            throw new FacultyNotFoundException("Факультет с id " + id +
                    " не найден в списке");
        }
        return removedFaculty;
    }

    @Override
    public Collection<Faculty> readByColor(String color) {
        return faculties.values().stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .collect(Collectors.toUnmodifiableList());
    }
}

