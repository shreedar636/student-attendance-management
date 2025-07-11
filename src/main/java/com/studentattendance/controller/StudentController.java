package com.studentattendance.controller;

import com.studentattendance.dtos.CreateStudentRequest;
import com.studentattendance.dtos.CreateTeacherRequest;
import com.studentattendance.dtos.StudentResponse;
import com.studentattendance.dtos.TeacherResponse;
import com.studentattendance.entity.Attendance;
import com.studentattendance.entity.Student;
import com.studentattendance.errorhandler.ServiceResult;
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

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> studentRegister(@RequestBody CreateStudentRequest request) {
        ServiceResult<StudentResponse> result = studentService.createStudent(request);
        return ResponseEntity.status(result.getData().getCode()).body(result.getData());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        ServiceResult<Boolean> result = studentService.deleteTeacher(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Optional<Student> student = studentService.getStudentById(id);
        return student.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public ResponseEntity<Student> getCurrentStudent() {
        Optional<Student> student = studentService.getCurrentStudent();
        return student.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/attendance")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER') or (hasRole('ROLE_STUDENT') and #id == principal.id)")
    public List<Attendance> getStudentAttendance(@PathVariable Long id) {
        return attendanceService.getAttendanceByStudentId(id);
    }

    @GetMapping("/{id}/attendance/percentage")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER') or (hasRole('ROLE_STUDENT') and #id == principal.id)")
    public double getAttendancePercentage(@PathVariable Long id) {
        return attendanceService.calculateAttendancePercentage(id);
    }

}