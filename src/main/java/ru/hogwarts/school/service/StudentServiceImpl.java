package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;
import java.util.Collection;
import java.util.Optional;

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
    public Collection<Student> readByAge(int age) {
        return students.findAllByAge(age);
    }

    @Override
    public Collection<Student> readByAgeBetween(int minAge, int maxAge) {
        return students.getByAgeBetween(minAge, maxAge);
    }
    @Override
    public Faculty readStudentFaculty(long studentId) {
        return read(studentId).getFaculty();
    }
    @Override
    public Collection<Student> readByFacultyId(long facultyId) {
        return students.findAllByFaculty_id(facultyId);
    }

}
