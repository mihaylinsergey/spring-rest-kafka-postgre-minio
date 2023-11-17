package com.clientdemo.client.mapper;

import com.clientdemo.client.dto.StudentDTO;
import com.clientdemo.client.model.Student;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class JsonToStudentDTOMapper {

    public StudentDTO getStudentDTO(String studentJson) throws JsonProcessingException {
        Student student = new ObjectMapper().readValue(studentJson, Student.class);
        byte[] backToBytes = Base64.decodeBase64(student.getPhoto());
        log.info("Полученные в формате JSON данные преобразуются в формат Student");
        return StudentDTO.of()
                .number(student.getNumber())
                .faculty(student.getFaculty())
                .lastName(student.getLastName())
                .firstName(student.getFirstName())
                .patronymic(student.getPatronymic())
                .photo(backToBytes)
                .build();
    }

    public List<StudentDTO> getListStudentDTO(String studentsJson) throws JsonProcessingException {
        TypeReference<List<Student>> typeReference = new TypeReference<List<Student>>() { };
        log.info("Полученные в формате JSON данные преобразуются в формат <List<Student>");
        return new ObjectMapper().readValue(studentsJson, typeReference).stream()
                .map(x -> StudentDTO.of()
                .number(x.getNumber())
                .faculty(x.getFaculty())
                .lastName(x.getLastName())
                .firstName(x.getFirstName())
                .patronymic(x.getPatronymic())
                .photo(Base64.decodeBase64(x.getPhoto()))
                .build())
                .collect(Collectors.toList());
    }
}
