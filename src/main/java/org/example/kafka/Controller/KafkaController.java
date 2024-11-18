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

    @PostMapping("/send")
    public String sendMessage(@RequestParam String topicId, @RequestParam(required = false) Integer partitionId,
                              @RequestParam(required = false) String uniqueKey, @RequestBody Employee employee) {
        messageProducer.sendMessage(topicId, employee, partitionId, uniqueKey);
        log.info("Message Data: {}", employee);
        return "Message sent";
    }

    @PostMapping("/autosending")
    public String sendMessageAuto(@RequestParam String topicId) {
        messageProducer.sendMessageAuto(topicId);
        return "Message sent";
    }

    @PostMapping("/sendRandom")
    public String sendMessageAutoRandom(@RequestParam String topicId) {
        messageProducer.sendMessageAutoRandom(topicId);
        return "Message sent";
    }
}