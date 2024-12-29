package com.ust.CourseService.controller;

import com.ust.CourseService.dto.ResponseDto;
import com.ust.CourseService.entity.Course;
import com.ust.CourseService.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.web.bind.annotation.*;
//import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // Create a new course
    @PostMapping
    public Course createCourse(@RequestBody @Valid Course course) {
        return courseService.createCourse(course);
    }

    // Update an existing course
    @PutMapping("/{courseId}")
    public Course updateCourse(@PathVariable int courseId, @RequestBody @Valid Course course) {
        return courseService.updateCourse(courseId, course);
    }

    // Delete a course by ID
    @DeleteMapping("/{courseId}")
    public void deleteCourse(@PathVariable int courseId) {
        courseService.deleteCourse(courseId);
    }

    // Retrieve all courses
    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    // Retrieve a specific course by ID
    @GetMapping("/{courseId}")
    public Course getCourseById(@PathVariable int courseId) {
        return courseService.getCourseById(courseId);
    }

    // Retrieve students enrolled in a specific course
    @GetMapping("/{courseId}/students")
    public ResponseDto getStudents(@PathVariable int courseId) {
        return courseService.getStudents(courseId);
    }

    @GetMapping("/exists/{id}")
    public boolean doesCourseExist(@PathVariable int id) {
        // Logic to check if the course exists
        return courseService.existsById(id);
    }
}

