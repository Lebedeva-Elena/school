package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student create(@RequestBody Student student) {
        return studentService.create(student);
    }

    @GetMapping("/{id}")
    public Student read(@PathVariable long id) {
        return studentService.read(id);

    }

    @PutMapping
    public Student update(@RequestBody Student student) {
        return studentService.update(student);

    }

    @DeleteMapping("/{id}")
    public Student delete(@PathVariable long id) {
        return studentService.delete(id);
    }

    @GetMapping
    public Collection<Student> readByAge(@RequestParam int age) {
        return studentService.readByAge(age);
    }

    @GetMapping("/age")
    public Collection<Student> readByAgeBetween(@RequestParam int minAge, @RequestParam int maxAge) {
        return studentService.readByAgeBetween(minAge, maxAge);
    }

    @GetMapping("/student_id")
    public Faculty readStudentFaculty(@RequestParam long studentId) {
        return studentService.readStudentFaculty(studentId);

    }

    @GetMapping("/faculty_id")
    public Collection<Student> readByFaculty_id(@RequestParam long facultyId) {
        return studentService.readByFacultyId(facultyId);
    }

}
