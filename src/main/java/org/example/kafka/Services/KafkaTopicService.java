package org.example.kafka.Services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaTopicService {

    private final AdminClient adminClient;
    private final ConsumerFactory<String, Object> consumerFactory;



    public void createTopic(String topicName, int partitions, short replicationFactor) {
        NewTopic newTopic = TopicBuilder.name(topicName)
                .partitions(partitions)
                .replicas(replicationFactor)
                .build();
        try {
            adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
            System.out.println("Topic created: " + topicName);
        } catch (ExecutionException | InterruptedException e) {
            log.error("Error Message:"+e.getMessage());
        }
    }

    public ResponseEntity<?> fetchMessagesFromTopic(String topic) {
        if (topic == null) {
            return ResponseEntity.badRequest().body("Invalid request: topicId is required.");
        }
        List<Object> messages = new ArrayList<>();
        try (Consumer<String, Object> consumer = consumerFactory.createConsumer()) {
            consumer.subscribe(Collections.singletonList(topic));
            ConsumerRecords<String, Object> records = consumer.poll(Duration.ofMillis(2000));

            for (ConsumerRecord<String, Object> record : records) {
                messages.add(record.value());
            }
        } catch (Exception e) {
            log.error("Error fetching messages from topic {}: {}", topic, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch messages.");
        }

        return ResponseEntity.ok(messages);
    }

//    @KafkaListener(topics = "sigma", groupId = "${spring.kafka.consumer.group-id}")
//    private void getSigmaMessage(ConsumerRecord<String, Object> record){
//        log.info("Received message from topic 'sigma':");
//        log.info("Key: {}", record.key());
//        log.info("Value: {}", record.value());
//        log.info("Partition: {}", record.partition());
//        log.info("Offset: {}", record.offset());
//    }

}