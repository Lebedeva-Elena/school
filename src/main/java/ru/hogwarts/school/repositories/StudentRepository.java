package ru.hogwarts.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Collection<Student> findAllByAge(int age);
    Collection<Student> getByAgeBetween(int minAge, int maxAge);

    Collection<Student> findAllByFaculty_id(long facultyId);


}

