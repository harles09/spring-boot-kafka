package org.example.kafka.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kafka.Model.Employee;
import org.example.kafka.Services.MessageConsumerService;
import org.example.kafka.Services.MessageProducerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/message")
@Slf4j
public class KafkaController {

    private final MessageProducerService messageProducer;
    private final MessageConsumerService messageConsumer;

    @PostMapping("/send/{topicId}")
    public String sendMessage(@PathVariable String topicId, @RequestBody Employee employee) {
        messageProducer.sendMessage(topicId, employee);
        log.info("Message Data: {}", employee);
        return "Message sent";
    }

    @GetMapping
    public List<Object> getMessages(@RequestParam(defaultValue = "100",required = false) int count,@RequestParam String topicId ) {
        return messageConsumer.getLastMessages(count, topicId);
    }

}