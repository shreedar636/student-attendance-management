package com.studentattendance.controller;

import com.studentattendance.entity.Attendance;
import com.studentattendance.entity.Student;
import com.studentattendance.service.AttendanceService;
import com.studentattendance.service.StudentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
@Tag(name = "3. Student", description = "Student Api")
public class StudentController {
    private final StudentService studentService;
    private final AttendanceService attendanceService;

    public StudentController(StudentService studentService, AttendanceService attendanceService) {
        this.studentService = studentService;
        this.attendanceService = attendanceService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Optional<Student> student = studentService.getStudentById(id);
        return student.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Student> getCurrentStudent() {
        Optional<Student> student = studentService.getCurrentStudent();
        return student.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/attendance")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or (hasRole('STUDENT') and #id == principal.id)")
    public List<Attendance> getStudentAttendance(@PathVariable Long id) {
        return attendanceService.getAttendanceByStudentId(id);
    }

    @GetMapping("/{id}/attendance/percentage")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or (hasRole('STUDENT') and #id == principal.id)")
    public double getAttendancePercentage(@PathVariable Long id) {
        return attendanceService.calculateAttendancePercentage(id);
    }
}