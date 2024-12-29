package com.ust.SudentService.service;



import com.ust.SudentService.entity.Student;
import com.ust.SudentService.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private WebClient webClient; // To communicate with Course Service

    // 1. Create a new student
    public Student createStudent(Student student) {
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new IllegalArgumentException("Student with email already exists: " + student.getEmail());
        }

        validateCourse(student.getCourseId()); // Validate course existence via Course Service

        Student savedStudent = studentRepository.save(student);
        return savedStudent;
    }

    // 2. Get a student by ID
    public Student getStudentById(int id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + id));
        return student;
    }

    // 3. Get a student by email
    public Student getStudentByEmail(String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with email: " + email));
        return student;
    }

    // 4. Get all students
    public List<Student> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students;
    }

    // 5. Update student details
    public Student updateStudent(int id, Student pstudent) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + id));

        if (!student.getEmail().equals(pstudent.getEmail())
                && studentRepository.existsByEmail(pstudent.getEmail())) {
            throw new IllegalArgumentException("Email is already in use: " + pstudent.getEmail());
        }

        validateCourse(pstudent.getCourseId()); // Validate course existence via Course Service

        student.setName(pstudent.getName());
        student.setEmail(pstudent.getEmail());
        student.setCourseId(pstudent.getCourseId());

        Student updatedStudent = studentRepository.save(student);
        return updatedStudent;
    }

    // 6. Delete a student by ID
    public void deleteStudent(int id) {
        if (!studentRepository.existsById(id)) {
            throw new EntityNotFoundException("Student not found with ID: " + id);
        }
        studentRepository.deleteById(id);
    }

    // 7. Find all students by course ID
    public List<Student> getStudentsByCourseId(int courseId) {
        validateCourse(courseId); // Validate course existence via Course Service
        List<Student> students = studentRepository.findByCourseId(courseId);
        return students;
    }


    // 8. Validate if a course exists (via Course Service)
    private void validateCourse(int courseId) {
        if (courseId != 0) {
            Boolean courseExists = webClient.get()
                    .uri("http://localhost:8082/api/courses/exists/"+courseId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            if (courseExists == null || !courseExists) {
                throw new IllegalArgumentException("Invalid course ID: " + courseId);
            }
        }
    }


}
