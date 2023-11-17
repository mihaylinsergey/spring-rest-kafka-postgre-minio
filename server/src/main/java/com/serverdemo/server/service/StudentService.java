package com.serverdemo.server.service;

import com.serverdemo.server.model.Student;
import com.serverdemo.server.repository.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class StudentService {

    private StudentRepository studentRepository;

    public List<Student> getAllUnits() {
        return studentRepository.findAll();
    }

    public Optional<Student> getOneUnit(String number) {
        return studentRepository.findByNumber(number);
    }
}
