package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.List;

public interface FacultyService {



    Faculty create(Faculty faculty);

    Faculty read(long id);

    Faculty update(Faculty faculty);

    Faculty delete(long id);


    Collection<Faculty> readByColor(String color);

    Collection<Faculty> readByNameContainingIgnoreCaseOrColorContainingIgnoreCase(String name, String color);
}
