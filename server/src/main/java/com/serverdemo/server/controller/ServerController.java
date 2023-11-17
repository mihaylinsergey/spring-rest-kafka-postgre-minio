package com.serverdemo.server.controller;

import com.serverdemo.server.kafka.KafkaMessageSender;
import com.serverdemo.server.mapper.StudentToJsonMapper;
import com.serverdemo.server.model.KafkaMessage;
import com.serverdemo.server.model.Student;
import com.serverdemo.server.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Component
@AllArgsConstructor
public class ServerController {

    private final KafkaMessageSender kafkaMessageSender;
    private final StudentService studentService;

    private final StudentToJsonMapper mapper;

    @KafkaListener(topics = "getAllUnits", groupId = "${kafka.groupId}")
    public void getAllUnits(String text) {
        List<Student> list = studentService.getAllUnits();
        String listJson = mapper.getListStudentDTO(list);
        Runnable runnable = getUnit(text, listJson);
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @KafkaListener(topics = "getOneUnit", groupId = "${kafka.groupId}")
    public void getOneUnit(String text) throws FileNotFoundException {
        String number = text.split("/")[1];
        Optional<Student> optionalStudent = studentService.getOneUnit(number);
        Student student = optionalStudent.isPresent() ? optionalStudent.get() : new Student();
        String studentJson = mapper.getStudentDTO(student);
        Runnable runnable = getUnit(text, studentJson);
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public Runnable getUnit(String text, String unit) {
        String[] data = text.split("/");
        Runnable runnable =
                () -> {
                    System.out.println("Start requestId: " + data[0]);

                    try {
                        int sleepMs = ThreadLocalRandom.current().nextInt(0, 17000 + 1);
                        System.out.println("RequestId: " + data[0] + " sleep: " + sleepMs + "ms");
                        Thread.sleep(sleepMs);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    kafkaMessageSender.send(UUID.fromString(data[0]), new KafkaMessage<>(unit));
                    System.out.println("End requestId: " + data[0]);
                };
        return runnable;
    }
}
