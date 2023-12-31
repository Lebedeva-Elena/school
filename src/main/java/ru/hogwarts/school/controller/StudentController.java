package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("/student")
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

    @GetMapping("/count")
    public Integer getCountOfAllStudents() {
        return studentService.getCountOfAllStudents();

    }

    @GetMapping("/avg")
    public Double getAverageAgeOfStudents() {
        return studentService.getAverageAgeOfStudents();
    }
    @GetMapping("/last-five-students")
    public Collection<Student> getLastFiveStudents() {
        return studentService.getLastFiveStudents();
    }

    @GetMapping("/filtredbyname")
    public ResponseEntity<Collection<String>> getAllStudentsWithName() {
        Collection<String> stringCollection = studentService.getFilteredByName();
        if (stringCollection.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(stringCollection);
    }

    @GetMapping("/avgage")
    public Double getStudentsAvgAge() {
        return studentService.getStudentsAvgAge();

    }

}

