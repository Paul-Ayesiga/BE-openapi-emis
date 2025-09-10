package com.emis_app.emis_app.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AgentTaskConsumer {

    @KafkaListener(topics = "agent.responses", groupId = "agent-consumer")
    public void listen(String message) {
        System.out.println("Agent response: " + message);
    }
}