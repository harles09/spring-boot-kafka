package org.example.kafka.Model;

import lombok.Data;

@Data
public class TopicRequest {
    private String topicName;
    private int partitions;
    private short replicationFactor;
}
