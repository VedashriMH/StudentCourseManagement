package com.ust.CourseService.service;

import com.ust.CourseService.dto.CourseDto;
import com.ust.CourseService.dto.ResponseDto;
import com.ust.CourseService.dto.StudentsDto;
import com.ust.CourseService.entity.Course;
import com.ust.CourseService.repository.CourseRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
//import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private WebClient webClient;


    // Create a new course
    public Course createCourse(@Valid Course course) {
        // Check if course name already exists
        Optional<Course> existingCourse = courseRepository.findByName(course.getName());
        if (existingCourse.isPresent()) {
            throw new IllegalArgumentException("Course with name '" + course.getName() + "' already exists.");
        }

        // Save the new course to the repository
        Course savedCourse = courseRepository.save(course);

        // Convert the saved entity back to a DTO and return
        return savedCourse;
    }

    // Update an existing course
    public Course updateCourse(int courseId, @Valid Course newCourse) {
        // Find the course by ID
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));

        // Check if course name already exists
        Optional<Course> existingCourse = courseRepository.findByName(newCourse.getName());
        if (existingCourse.isPresent() && existingCourse.get().getId() != courseId) {
            throw new IllegalArgumentException("Course with name '" + newCourse.getName() + "' already exists.");
        }

        // Update course details
        course.setName(newCourse.getName());
        course.setDescription(newCourse.getDescription());

        // Save the updated course to the repository
        Course updatedCourse = courseRepository.save(course);

        return updatedCourse;
    }

    // Delete a course by ID
    public void deleteCourse(int courseId) {
        // Check if course exists before attempting to delete
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));

        // Delete course
        courseRepository.delete(course);
    }

    // Retrieve a list of all courses
    public List<Course> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        // Convert the list of Course entities to a list of CourseDto objects
        return courses;
    }

    // Retrieve a course by its ID
    public Course getCourseById(int courseId) {
        // Find course by ID
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));

        // Return the course as a DTO
        return course;
    }


    public ResponseDto getStudents(int courseId) {
        ResponseDto responseDto = new ResponseDto();
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        CourseDto courseDto = mapToCourseDto(course);

        List<StudentsDto> StudentDtolist = webClient.get()
                .uri("http://localhost:8081/api/students/course/" + course.getId())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<StudentsDto>>() {})
                .block();

        responseDto.setCourseDto(courseDto);
        responseDto.setStudentsDtos(StudentDtolist);

        return responseDto;
    }

    public ResponseDto fallbackGetGym(int courseId, Throwable throwable) {
        // Handle the fallback logic, e.g., return a default response or log the error
        return new ResponseDto(); // Return an empty response or a default response
    }

    private CourseDto mapToCourseDto(Course course) {
        CourseDto courseDto = new CourseDto();
        courseDto.setId(course.getId());
        courseDto.setName(course.getName());
        courseDto.setDescription(course.getDescription());

        return courseDto;
    }

    public Boolean existsById(int id){
        return courseRepository.existsById(id);
    }

}

