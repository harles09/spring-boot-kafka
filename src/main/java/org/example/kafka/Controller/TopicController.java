package org.example.kafka.Controller;

import lombok.RequiredArgsConstructor;
import org.example.kafka.Model.TopicRequest;
//import org.example.kafka.Services.KafkaTopicService;
import org.example.kafka.Services.KafkaTopicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
public class TopicController {

    private final KafkaTopicService kafkaTopicService;

    @PostMapping("/create")
    public String createTopic(@RequestBody TopicRequest topicRequest) {
        kafkaTopicService.createTopic(topicRequest.getTopicName(), topicRequest.getPartitions(), topicRequest.getReplicationFactor());
        return "Topic created: " + topicRequest.getTopicName();
    }

    @PostMapping("/messages")
    public ResponseEntity<?> fetchMessages(@RequestParam String topic) {
        return kafkaTopicService.fetchMessagesFromTopic(topic);
    }
}