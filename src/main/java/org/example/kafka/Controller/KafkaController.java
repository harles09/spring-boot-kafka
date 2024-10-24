package org.example.kafka.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kafka.Model.Employee;
import org.example.kafka.Services.MessageConsumerService;
import org.example.kafka.Services.MessageProducerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class KafkaController {

    @Value("${spring.kafka.topic.id}")
    private String topicId;

    private final MessageProducerService messageProducer;
    private final MessageConsumerService messageConsumerService;

    @PostMapping("/send")
    public String sendMessage(@RequestBody Employee employee) {
        messageProducer.sendMessage(topicId, employee);
        log.info("Message Data: {}", employee);
        return "Message sent";
    }

}