package com.clientdemo.client.controller;

import com.clientdemo.client.dto.StudentDTO;
import com.clientdemo.client.mapper.JsonToStudentDTOMapper;
import com.clientdemo.client.service.SyncKafkaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.concurrent.TimeoutException;

@RestController
@AllArgsConstructor
@Slf4j
public class ClientController {

    private final SyncKafkaService syncKafkaService;

    private final JsonToStudentDTOMapper mapper;

    @GetMapping("/getAllUnits")
    public ResponseEntity<List<StudentDTO>> getAllUnits() throws JsonProcessingException {
        log.info("Получен запрос getAllUnits");
        String result = null;
        try {
            result = syncKafkaService.get(null);
        } catch (TimeoutException e) {
            return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
        }
        log.info(String.format("В браузер передается результат %s", result));
        return ResponseEntity.ok(mapper.getListStudentDTO(result));
    }

    @GetMapping("/getOneUnit/{number}")
    public ResponseEntity<StudentDTO> getOneUnit(@PathVariable("number") String number) throws JsonProcessingException {
        log.info(String.format("Получен запрос getOneUnit/%s", number));
        String result = null;
        try {
            result = syncKafkaService.get(number);
        } catch (TimeoutException e) {
            return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
        }
        log.info(String.format("В браузер передается результат %s", result));
        return ResponseEntity.ok(mapper.getStudentDTO(result));
    }

}
