package com.serverdemo.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@Builder(builderMethodName = "of")
@ToString
public class StudentDTO {

    private String number;

    private String faculty;

    private String lastName;

    private String firstName;

    private String patronymic;

    private  String photo;
}
