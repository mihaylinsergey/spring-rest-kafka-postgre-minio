package com.serverdemo.server.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverdemo.server.dto.StudentDTO;
import com.serverdemo.server.model.Student;
import com.serverdemo.server.service.MinioService;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class StudentToJsonMapper {

    private final MinioService minioService;

    public String getStudentDTO(Student student) {
        byte[] photo = minioService.getFile(String
                .format("%s.jpg", student.getNumber())).orElse(new byte[0]);
        String base64String = Base64.encodeBase64String(photo);
        StudentDTO studentDTO = StudentDTO.of()
                .number(student.getNumber())
                .faculty(student.getFaculty())
                .lastName(student.getLastName())
                .firstName(student.getFirstName())
                .patronymic(student.getPatronymic())
                .photo(base64String)
                .build();
        String studentJson;
        try {
            studentJson = new ObjectMapper().writeValueAsString(studentDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return studentJson;
    }

    public String getListStudentDTO(List<Student> students) {
        List<StudentDTO> studentsDTO = students.stream()
                .map(x -> StudentDTO.of()
                .number(x.getNumber())
                .faculty(x.getFaculty())
                .lastName(x.getLastName())
                .firstName(x.getFirstName())
                .patronymic(x.getPatronymic())
                .photo(Base64.encodeBase64String(minioService
                        .getFile(String.format("%s.jpg", x.getNumber())).orElse(new byte[0])))
                .build())
                .collect(Collectors.toList());
        String studentsJson;
        try {
            studentsJson = new ObjectMapper().writeValueAsString(studentsDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return studentsJson;
    }
}
