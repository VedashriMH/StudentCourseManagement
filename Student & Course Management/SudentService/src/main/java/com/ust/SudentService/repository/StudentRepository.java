package com.ust.SudentService.repository;

import com.ust.SudentService.entity.Student;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    boolean existsByEmail(@NotBlank(message = "Email cannot be blank") @Email(message = "Email should be valid") String email);


    Optional<Student> findByEmail(String email);

    List<Student> findByCourseId(int courseId);

    List<Student> findByCourseIdIsNull();
}
