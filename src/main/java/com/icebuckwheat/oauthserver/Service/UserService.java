package com.icebuckwheat.oauthserver.Service;

import com.icebuckwheat.oauthserver.Dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final KafkaTemplate<String, User> kafkaTemplate;

    @Autowired
    public UserService(KafkaTemplate<String, User> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserId(User user) {
        kafkaTemplate.send("user-topics", user);  // "user-topic"은 Kafka 토픽 이름
    }
}