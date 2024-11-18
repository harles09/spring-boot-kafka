package org.example.kafka.Services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class MessageProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Random random = new Random();

    public void sendMessage(String topic, Object message, Integer partitionId, String uniqueKey) {
        kafkaTemplate.send(topic,partitionId,uniqueKey,message);
    }


    public void sendMessageAuto(String topic) {
        String randomMessage = generateRandomMessage();
        kafkaTemplate.send(topic, randomMessage);
    }

    public void sendMessageAutoRandom(String topic) {
        String randomKey = UUID.randomUUID().toString();
        String randomMessage = generateRandomMessage();
        kafkaTemplate.send(topic, randomKey, randomMessage);
    }

    private String generateRandomMessage() {
        int length = 10;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }
}