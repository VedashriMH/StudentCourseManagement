package com.ust.CourseService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {
    public CourseDto getCourseDto() {
        return courseDto;
    }

    public void setCourseDto(CourseDto courseDto) {
        this.courseDto = courseDto;
    }

    public List<StudentsDto> getStudentsDtos() {
        return studentsDtos;
    }

    public void setStudentsDtos(List<StudentsDto> studentsDtos) {
        this.studentsDtos = studentsDtos;
    }

    public CourseDto courseDto;
    public List<StudentsDto> studentsDtos;
}
