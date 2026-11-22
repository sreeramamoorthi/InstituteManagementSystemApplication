package repository;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Student> findByCourseIgnoreCase(String course);

    List<Student> findByDepartmentIgnoreCase(String department);

    List<Student> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName);
}