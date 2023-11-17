package com.serverdemo.server.repository;

import com.serverdemo.server.model.Student;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends CrudRepository<Student, Integer> {

    List<Student> findAll();

    Optional<Student> findByNumber(String number);
}
