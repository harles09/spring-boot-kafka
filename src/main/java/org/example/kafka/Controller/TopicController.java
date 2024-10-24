package org.example.kafka.Controller;

import lombok.RequiredArgsConstructor;
import org.example.kafka.Model.TopicRequest;
import org.example.kafka.Services.KafkaTopicService;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
public class TopicController {

    private final KafkaTopicService kafkaTopicService;
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private ConcurrentKafkaListenerContainerFactory<String, String> factory;

    @PostMapping("/create")
    public String createTopic(@RequestBody TopicRequest topicRequest) {
        kafkaTopicService.createTopic(topicRequest.getTopicName(), topicRequest.getPartitions(), topicRequest.getReplicationFactor());
        return "Topic created: " + topicRequest.getTopicName();
    }

    @PostMapping("/changeTopic")
    public String changeTopic(@RequestBody String newTopic) {
        return kafkaTopicService.changeTopic(newTopic);
    }
}