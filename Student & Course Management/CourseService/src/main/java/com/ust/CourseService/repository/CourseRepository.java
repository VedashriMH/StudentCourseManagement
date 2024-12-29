package com.ust.CourseService.repository;

import com.ust.CourseService.entity.Course;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer > {
    Optional<Course> findByName(@NotBlank(message = "Name cannot be blank") @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters") String name);
}
