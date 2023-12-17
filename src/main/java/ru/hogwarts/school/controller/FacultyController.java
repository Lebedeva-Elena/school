package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.stream.Stream;


@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public Faculty create(@RequestBody Faculty faculty) {
        return facultyService.create(faculty);
    }

    @GetMapping("/{id}")
    public Faculty read(@PathVariable Long id) {
        return facultyService.read(id);
    }

    @PutMapping
    public Faculty update(@RequestBody Faculty faculty) {
        facultyService.update(faculty);
        return faculty;
    }

    @DeleteMapping("/{id}")
    public Faculty delete(@PathVariable long id) {
        return facultyService.delete(id);
    }

    @GetMapping("/color")
    public Collection<Faculty> readByColor(@RequestParam String color) {
        return facultyService.readByColor(color);
    }

    @GetMapping("/filter")
    public Collection<Faculty> readByNameContainingIgnoreCaseOrColorContainingIgnoreCase(@RequestParam String name,
                                                                                         @RequestParam String color) {
        return facultyService.readByNameContainingIgnoreCaseOrColorContainingIgnoreCase(name, color);

    }

    @GetMapping("/mustlongnameofaculty")
    public ResponseEntity<String> getMustLongNameFaculty() {
        return facultyService.getMustLongNameFaculty();
    }

    @GetMapping("/sum")
    public int getSum() {
        long time = System.currentTimeMillis();
        Stream
                .iterate(1, a -> a + 1)
                .limit(1_000_000)
                .reduce(0, Integer::sum);
        time = System.currentTimeMillis() - time;
        System.out.printf("time %d \n", time);
        return (int) time;
    }


}

