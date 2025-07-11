package com.studentattendance.controller;

import com.studentattendance.dtos.CreateTeacherRequest;
import com.studentattendance.dtos.TeacherResponse;
import com.studentattendance.entity.Attendance;
import com.studentattendance.entity.Teacher;
import com.studentattendance.errorhandler.ServiceResult;
import com.studentattendance.service.AttendanceService;
import com.studentattendance.service.TeacherService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/teachers")
@Tag(name = "4. Teacher", description = "Teacher Api")
public class TeacherController {
    private final TeacherService teacherService;
    private final AttendanceService attendanceService;

    public TeacherController(TeacherService teacherService, AttendanceService attendanceService) {
        this.teacherService = teacherService;
        this.attendanceService = attendanceService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> teacherRegister(@RequestBody CreateTeacherRequest request) {
        ServiceResult<TeacherResponse> result = teacherService.createTeacher(request);
        return ResponseEntity.status(result.getData().getCode()).body(result.getData());
    }


    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Teacher> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable Long id) {
        Optional<Teacher> teacher = teacherService.getTeacherById(id);
        return teacher.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public ResponseEntity<Teacher> getCurrentTeacher() {
        Optional<Teacher> teacher = teacherService.getCurrentTeacher();
        return teacher.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/attendance")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public Attendance markAttendance(@RequestParam Long studentId,
                                     @RequestParam LocalDate date,
                                     @RequestParam String status,
                                     @RequestParam(required = false) String remarks) {
        return attendanceService.markAttendance(studentId, date, status, remarks);
    }

    @GetMapping("/attendance")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public List<Attendance> getAttendanceByDate(@RequestParam LocalDate date) {
        return attendanceService.getAttendanceByDate(date);
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        ServiceResult<Boolean> result = teacherService.deleteTeacher(id);
        return ResponseEntity.ok().build();
    }
}