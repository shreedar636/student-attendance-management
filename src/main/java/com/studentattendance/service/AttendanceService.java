package com.studentattendance.service;

import com.studentattendance.entity.Attendance;
import com.studentattendance.entity.Student;
import com.studentattendance.entity.Teacher;
import com.studentattendance.repositories.AttendanceRepository;
import com.studentattendance.repositories.StudentRepository;
import com.studentattendance.repositories.TeacherRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             StudentRepository studentRepository,
                             TeacherRepository teacherRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    public Attendance markAttendance(Long studentId, LocalDate date, String status, String remarks) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Teacher teacher = teacherRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Attendance attendance = new Attendance();
        attendance.setFirstName(student.getFirstName());
        attendance.setLastName(student.getLastName());
        attendance.setDate(date);
        attendance.setStatus(status);
        attendance.setRemarks(remarks);
        attendance.setStudent(student);
        attendance.setTeacher(teacher);

        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getAttendanceByStudentId(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }

    public List<Attendance> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByDate(date);
    }

    public double calculateAttendancePercentage(Long studentId) {
        List<Attendance> allRecords = attendanceRepository.findByStudentId(studentId);
        if (allRecords.isEmpty()) return 0.0;

        long presentCount = allRecords.stream()
                .filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus()))
                .count();

        return (presentCount * 100.0) / allRecords.size();
    }
}