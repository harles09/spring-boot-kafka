package org.example.kafka.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kafka.Model.Employee;
import org.example.kafka.Services.MessageProducerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/message")
@Slf4j
public class KafkaController {

    private final MessageProducerService messageProducer;

    @PostMapping("/send/{topicId}")
    public String sendMessage(@PathVariable String topicId, @RequestBody Employee employee) {
        messageProducer.sendMessage(topicId, employee);
        log.info("Message Data: {}", employee);
        return "Message sent";
    }

    @PostMapping("/autosending/{topicId}")
    public String sendMessageAuto(@PathVariable String topicId) {
        messageProducer.sendMessageAuto(topicId);
        return "Message sent";
    }

    @PostMapping("/sendRandom/{topicId}")
    public String sendMessageAutoRandom(@PathVariable String topicId) {
        messageProducer.sendMessageAutoRandom(topicId);
        return "Message sent";
    }
}