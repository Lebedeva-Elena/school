package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.StudentAlreadyExistsException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    private final Map<Long, Student> students = new HashMap<>();
    private long lastId = 0;
    @Override
    public Student create(Student student) {
        if (students.containsValue(student)) {
            throw new StudentAlreadyExistsException("Студент " + student +
                    " уже был добавлен");
        }
        long id = ++lastId;
        student.setId(id);

        students.put(id, student);
        return student;
    }
    @Override
    public Student read(long id) {
        Student student = students.get(id);
        if (student == null) {
            throw new StudentNotFoundException("Студент с id " + id +
                    " не найден в списке");
        }
        return student;

    }
    @Override
    public Student update(Student student) {
        if (!students.containsKey(student.getId())) {
            throw new StudentNotFoundException("Студент с id " + student.getId() +
                    " не найден в списке");
        }
        students.put(student.getId(), student);
        return student;
    }
    @Override
    public Student delete(long id) {
        Student removedStudent = students.remove(id);
        if (removedStudent == null) {
            throw new StudentNotFoundException("Студент с id " + id +
                    " не найден в списке");
        }
        return removedStudent;
    }

    @Override
    public Collection<Student> readByAge(int age){
        return students.values().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toUnmodifiableList());
    }

}
