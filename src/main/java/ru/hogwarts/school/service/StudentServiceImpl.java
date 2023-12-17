package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    private final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student create(Student student) {
        logger.info("Was invoked method for create student");
        return studentRepository.save(student);
    }

    @Override
    public Student read(long id) {
        logger.info("Was invoked method for read student");
        logger.error("There is not student with id = " + id);
        return studentRepository.findById(id).
                orElseThrow(() -> new StudentNotFoundException("Студент с id " + id +
                        " не найден в списке"));
    }

    @Override
    public Student update(Student student) {
        read(student.getId());
        logger.info("Was invoked method for update student");
        return studentRepository.save(student);
    }

    @Override
    public Student delete(long id) {
        Student student = read(id);
        studentRepository.delete(student);
        logger.info("Was invoked method for delete student");
        return student;
    }

    @Override
    public Collection<Student> readByAge(int age) {
        logger.info("Was invoked method for readByAge student");
        return studentRepository.findAllByAge(age);
    }

    @Override
    public Collection<Student> readByAgeBetween(int minAge, int maxAge) {
        logger.info("Was invoked method for readByAgeBetween student");
        return studentRepository.getByAgeBetween(minAge, maxAge);
    }

    @Override
    public Faculty readStudentFaculty(long studentId) {
        logger.info("Was invoked method for readStudentFaculty faculty");
        return read(studentId).getFaculty();
    }

    @Override
    public Collection<Student> readByFacultyId(long facultyId) {
        logger.info("Was invoked method for readByFacultyId students");
        return studentRepository.findAllByFaculty_id(facultyId);
    }

    @Override
    public Integer getCountOfAllStudents() {
        logger.info("Was invoked method for getCountOfAllStudents students");
        return studentRepository.getCountOfAllStudents();
    }

    @Override
    public Double getAverageAgeOfStudents() {
        logger.info("Was invoked method for getAverageAgeOfStudents students");
        return studentRepository.getAverageAgeOfStudents();
    }

    @Override
    public Collection<Student> getLastFiveStudents() {
        logger.info("Was invoked method for getLastFiveStudents students");
        return studentRepository.getLastFiveStudents();
    }

    @Override
    public Collection<String> getFilteredByName() {
        return studentRepository.findAll()
                .stream()
                .map(Student::getName)
                .map(String::toUpperCase)
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
    @Override
    public void printParallelStudents() {
        Thread thread = new Thread(() -> {
            printName(3L);
            printName(4L);
        });
        thread.setName("Thread 1");
        Thread thread1 = new Thread(() -> {
            printName(5L);
            printName(6L);
        });
        thread1.setName("Thread 2");
            thread.start();
            thread1.start();

            printName(1L);
            printName(2L);
    }

    @SuppressWarnings("deprecation")
    private void printName(long id) {
        String studentName = studentRepository.getById(id).getName();
        System.out.println(studentName);

    }
}




