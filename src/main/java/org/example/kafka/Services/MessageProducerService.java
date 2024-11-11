package org.example.kafka.Services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@Service
@RequiredArgsConstructor
@Slf4j
public class MessageProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Random random = new Random();

    public void sendMessage(String topic, Object message) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, message);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Unable to send message [{}] due to : {}", message, ex.getMessage());
            } else {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info("Message [{}] sent to partition [{}] with offset [{}]",
                        message, metadata.partition(), metadata.offset());
            }
        });
    }


    public void sendMessageAuto(String topic) {
        String randomMessage = generateRandomMessage();
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, randomMessage);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Unable to send message [{}] due to : {}", randomMessage, ex.getMessage());
            } else {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info("Message [{}] sent to partition [{}] with offset [{}]",
                        randomMessage, metadata.partition(), metadata.offset());
            }
        });
    }

    public void sendMessageAutoRandom(String topic) {
        String randomKey = UUID.randomUUID().toString();
        String randomMessage = generateRandomMessage();
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, randomKey, randomMessage);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Unable to send message [{}] with key [{}] due to : {}", randomMessage, randomKey, ex.getMessage());
            } else {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info("Message [{}] with key [{}] sent to partition [{}] with offset [{}]",
                        randomMessage, randomKey, metadata.partition(), metadata.offset());
            }
        });
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