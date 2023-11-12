package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.StudentAlreadyExistsException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository students;

    public StudentServiceImpl(StudentRepository students) {
        this.students = students;
    }

    @Override
    public Student create(Student student) {
        return students.save(student);
    }
    @Override
    public Student read(long id) {
        return students.findById(id).
                orElseThrow(() -> new StudentNotFoundException("Студент с id " + id +
                    " не найден в списке"));
        }

    @Override
    public Student update(Student student) {
        read(student.getId());

        return students.save(student);
    }
    @Override
    public Student delete(long id) {
        Student student = read(id);
        students.delete(student);
        return student;
    }

    @Override
    public Collection<Student> readByAge(int age){
        return students.findAllByAge(age);
    }

}
