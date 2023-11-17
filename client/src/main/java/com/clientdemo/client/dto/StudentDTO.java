package com.clientdemo.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder(builderMethodName = "of")
@NoArgsConstructor
public class StudentDTO {

    private String number;

    private String faculty;

    private String lastName;

    private String firstName;

    private String patronymic;

    private  byte[] photo;
}
