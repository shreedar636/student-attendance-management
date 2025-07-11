package com.studentattendance.service;

import com.studentattendance.dtos.CreateStudentRequest;
import com.studentattendance.dtos.StudentResponse;
import com.studentattendance.entity.Student;
import com.studentattendance.entity.User;
import com.studentattendance.models.Role;
import com.studentattendance.repositories.StudentRepository;
import com.studentattendance.repositories.UserRepository;
import com.studentattendance.errorhandler.ServiceResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public StudentService(StudentRepository studentRepository,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Optional<Student> getCurrentStudent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return studentRepository.findByEmail(email);
    }


    public ServiceResult<StudentResponse> createStudent(CreateStudentRequest request) {
        try {

            if (userRepository.existsByEmail(request.getEmail()) ||
                    studentRepository.existsByEmail(request.getEmail())) {
                return ServiceResult.error(
                        new StudentResponse(400, "Email already exists"),
                        "EMAIL_EXISTS"
                );
            }


            User user = new User();
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRoles(Set.of(Role.STUDENT));
            userRepository.save(user);

            Student student = new Student();
            student.setFirstName(request.getFirstName());
            student.setLastName(request.getLastName());
            student.setEmail(request.getEmail());
            student.setPassword(passwordEncoder.encode(request.getPassword()));
            student.setStudentClass(request.getStudentClass());
            studentRepository.save(student);

            return ServiceResult.success(
                    new StudentResponse(200, "Student added successfully")
            );
        } catch (Exception e) {
            return ServiceResult.error(
                    new StudentResponse(500, "Failed to create student: " + e.getMessage()),
                    "STUDENT_CREATION_FAILED"
            );
        }
    }

    public ServiceResult<Boolean> deleteTeacher(Long id) {
        studentRepository.deleteById(id);
        return ServiceResult.success(true);
    }
    public List<Student> getStudentsByClass(String studentClass) {
        return studentRepository.findByStudentClass(studentClass);
    }
}