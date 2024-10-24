package org.example.kafka.Services;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaCostumerManager {

    private KafkaConsumer<String, String> consumer;

    @Autowired
    private ApplicationEventPublisher publisher;

    @EventListener(ContextRefreshedEvent.class)
    public void handleContextRefresh() {
        try {
            // Gracefully shutdown the consumer
            consumer.wakeup();
            consumer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
