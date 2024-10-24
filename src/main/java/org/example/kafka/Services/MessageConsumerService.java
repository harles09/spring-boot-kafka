package org.example.kafka.Services;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageConsumerService {

    @KafkaListener(topics = "test-topic-0", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(ConsumerRecord<String, String> record) {
        System.out.println("Received message: " + record.value() + " from topic: " + record.topic());
    }
}