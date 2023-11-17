package com.clientdemo.client.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder(builderMethodName = "of")
@NoArgsConstructor
public class Student {

    private String number;

    private String faculty;

    private String lastName;

    private String firstName;

    private String patronymic;

    private  String photo;
}
