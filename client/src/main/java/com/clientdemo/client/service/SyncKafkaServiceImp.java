package com.clientdemo.client.service;

import com.clientdemo.client.model.RequestDto;
import com.clientdemo.client.senderreceiver.SenderReceiverMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
public class SyncKafkaServiceImp implements SyncKafkaService {

    @Autowired
    private SenderReceiverMap<UUID, String> senderReceiverMap;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${endpointServer}")
    private String endpointServer;

    @Value("${timeout:0}")
    private Long timeout;

    public SyncKafkaServiceImp(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public String get(String text) throws TimeoutException {
        UUID requestId = UUID.randomUUID();
        log.info(String.format("Сгенерирован уникальный номер задачи %s", requestId));
        while (senderReceiverMap.containsKey(requestId)) {
            requestId = UUID.randomUUID();
        }

        String responseFromServer = this.sendText(requestId, text);
        log.info(String.format("Сформированный запрос передается в метод sendText для отправки Kafka", responseFromServer));
        Thread thread = senderReceiverMap.add(requestId, timeout);
        thread.start();
        log.info("Создается поток, ожидающий обратного ответа от сервера");
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String responseKafka;
        try {
            responseKafka = senderReceiverMap.get(requestId).getData();
        } catch (TimeoutException e) {
            throw e;
        } finally {
            senderReceiverMap.remove(requestId);
        }
        log.info(String.format("Получен обратный ответ от сервера %s", responseKafka));
        return responseKafka;
    }

    private String sendText(final UUID requestId, final String text) {
        String topic = (text == null) ? "getAllUnits" : "getOneUnit";
        kafkaTemplate.send(topic, String.format("%s/%s", requestId.toString(), text));
        log.info(String.format("Kafka отправил запрос серверу %s/%s", requestId, text));
        return topic;
    }
}
