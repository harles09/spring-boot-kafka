package org.example.kafka.Services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.*;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaTopicService {

    private final AdminClient adminClient;
    private final ConfigurableApplicationContext context;
    private String currentTopic = "initial-topic";
    private KafkaConsumer<String, String> consumer;
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

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

    public String changeTopic(String newTopic){
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);

        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class.getName());


        KafkaConsumer<String, String> newConsumer = new KafkaConsumer<>(props);
        newConsumer.subscribe(Collections.singletonList(newTopic));
        consumer = newConsumer;
        currentTopic = newTopic;

        return "Subscribed to new topic: " + newTopic;
    }
}