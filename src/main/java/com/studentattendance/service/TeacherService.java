package com.studentattendance.service;

import com.studentattendance.dtos.CreateTeacherRequest;
import com.studentattendance.dtos.TeacherResponse;
import com.studentattendance.entity.Teacher;
import com.studentattendance.entity.User;
import com.studentattendance.errorhandler.ServiceResult;
import com.studentattendance.models.Role;
import com.studentattendance.repositories.TeacherRepository;
import com.studentattendance.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public TeacherService(TeacherRepository teacherRepository,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Optional<Teacher> getTeacherById(Long id) {
        return teacherRepository.findById(id);
    }

    public Optional<Teacher> getCurrentTeacher() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return teacherRepository.findByEmail(email);
    }

    @Transactional
    public ServiceResult<TeacherResponse> createTeacher(CreateTeacherRequest request) {
        try {

            if (userRepository.existsByEmail(request.getEmail()) ||
                    teacherRepository.existsByEmail(request.getEmail())) {
                return ServiceResult.error(
                        new TeacherResponse(400, "Email already exists"),
                        "EMAIL_EXISTS"
                );
            }


            User user = new User();
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRoles(Set.of(Role.TEACHER));
            userRepository.save(user);


            Teacher teacher = new Teacher();
            teacher.setFirstName(request.getFirstName());
            teacher.setLastName(request.getLastName());
            teacher.setEmail(request.getEmail());
            teacher.setPassword(passwordEncoder.encode(request.getPassword()));
            teacherRepository.save(teacher);

            return ServiceResult.success(
                    new TeacherResponse(200, "Teacher added successfully")
            );
        } catch (Exception e) {
            return ServiceResult.error(
                    new TeacherResponse(500, "Failed to create teacher: " + e.getMessage()),
                    "TEACHER_CREATION_FAILED"
            );
        }
    }
    public ServiceResult<Boolean> deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
        return ServiceResult.success(true);
    }

}