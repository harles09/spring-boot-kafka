package org.example.kafka.Controller;

import lombok.RequiredArgsConstructor;
import org.example.kafka.Services.KafkaTopicService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
public class TopicController {

    private KafkaTopicService kafkaTopicService;

    @PostMapping("/create")
    public String createTopic(@RequestParam String topicName,
                              @RequestParam int partitions,
                              @RequestParam short replicationFactor) {
        kafkaTopicService.createTopic(topicName, partitions, replicationFactor);
        return "Topic created: " + topicName;
    }
}