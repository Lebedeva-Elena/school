package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;
import java.util.Collection;

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
        Student studentDB = read(student.getId());
        studentDB.setName(student.getName());
        studentDB.setAge(studentDB.getAge());

        return studentRepository.save(studentDB);
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

}
