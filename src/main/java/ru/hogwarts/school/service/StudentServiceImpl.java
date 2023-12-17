package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student create(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student read(long id) {
        return studentRepository.findById(id).
                orElseThrow(() -> new StudentNotFoundException("Студент с id " + id +
                        " не найден в списке"));
    }

    @Override
    public Student update(Student student) {
        read(student.getId());

        return studentRepository.save(student);
    }

    @Override
    public Student delete(long id) {
        Student student = read(id);
        studentRepository.delete(student);
        return student;
    }

    @Override
    public Collection<Student> readByAge(int age) {
        return studentRepository.findAllByAge(age);
    }

    @Override
    public Collection<Student> readByAgeBetween(int minAge, int maxAge) {
        return studentRepository.getByAgeBetween(minAge, maxAge);
    }
    @Override
    public Faculty readStudentFaculty(long studentId) {
        return read(studentId).getFaculty();
    }
    @Override
    public Collection<Student> readByFacultyId(long facultyId) {
        return studentRepository.findAllByFaculty_id(facultyId);
    }

    @Override
    public Integer getCountOfAllStudents() {
        return studentRepository.getCountOfAllStudents();
    }

    @Override
    public Double getAverageAgeOfStudents() {
        return studentRepository.getAverageAgeOfStudents();
    }
    @Override
    public Collection<Student> getLastFiveStudents() {
        return studentRepository.getLastFiveStudents();
    }

    @Override
    public Collection<String> getFilteredByName() {
        return studentRepository.findAll()
                .stream()
                .map(Student :: getName)
                .map(String :: toUpperCase)
                .filter(s -> s.startsWith("A"))
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public Double getStudentsAvgAge() {
        return studentRepository.findAll()
                .stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0);
    }



}
