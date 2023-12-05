package ru.hogwarts.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Collection<Student> findAllByAge(int age);
    Collection<Student> getByAgeBetween(int minAge, int maxAge);

    Collection<Student> findAllByFaculty_id(long facultyId);

    @Query(value = "SELECT COUNT(*) FROM student", nativeQuery = true)
    Integer getCountOfAllStudents();

    @Query(value = "SELECT AVG(age) FROM student", nativeQuery = true)
    Double getAverageAgeOfStudents();

    @Query(value = "SELECT FROM * student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    Collection<Student> getLastFiveStudents();


}

