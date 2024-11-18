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
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaTopicService {

    private final AdminClient adminClient;
    private final ConsumerFactory<String, Object> consumerFactory;



    public void createTopic(String topicName, int partitions, short replicationFactor) {
        Map<String, String> configs = new HashMap<>();
        //7 hari
        configs.put("retention.ms", String.valueOf(7 * 24 * 60 * 60 * 1000L));
        NewTopic newTopic = TopicBuilder.name(topicName)
                .partitions(partitions)
                .replicas(replicationFactor)
                .configs(configs)
                .build();
        try {
            adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
            System.out.println("Topic created: " + topicName);
        } catch (ExecutionException | InterruptedException e) {
            log.error("Error Message:"+e.getMessage());
        }
    }

    public ResponseEntity<?> fetchMessagesFromTopic(String topic, String fromStart) {
        if (topic == null) {
            return ResponseEntity.badRequest().body("Invalid request: topicId is required.");
        }
        List<Object> messages = new ArrayList<>();
        try (Consumer<String, Object> consumer = consumerFactory.createConsumer()) {
            consumer.subscribe(Collections.singletonList(topic));
            if (fromStart != null){
                consumer.poll(Duration.ofMillis(100));
                consumer.seekToBeginning(consumer.assignment());
            }
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

//    @KafkaListener(topicPartitions = @TopicPartition(topic = "sigma", partitions = {"1"}))
//    private void getSigmaMessage(ConsumerRecord<String, Object> record){
//        log.info("Received message from topic 'sigma':");
//        log.info("Key: {}", record.key());
//        log.info("Value: {}", record.value());
//        log.info("Partition: {}", record.partition());
//        log.info("Offset: {}", record.offset());
//    }
//groupId = "${spring.kafka.consumer.group-id}"
    public ResponseEntity<?> deleteTopic(String topicName) {
        try {
            adminClient.deleteTopics(Collections.singletonList(topicName)).all().get();
            return ResponseEntity.ok("Topic " + topicName + " deleted successfully.");
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.ok("Error while deleting topic: " + e.getMessage());
        }
    }

}