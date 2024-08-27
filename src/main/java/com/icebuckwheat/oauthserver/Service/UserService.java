package com.icebuckwheat.oauthserver.Service;

import com.icebuckwheat.oauthserver.Dto.GetUserDataResponseDto;
import com.icebuckwheat.oauthserver.Dto.User;
import com.icebuckwheat.oauthserver.Entity.UserEntity;
import com.icebuckwheat.oauthserver.Repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final KafkaTemplate<String, User> kafkaTemplate;
    private final UserEntityRepository userEntityRepository;

    @Autowired
    public UserService(KafkaTemplate<String, User> kafkaTemplate, UserEntityRepository userEntityRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.userEntityRepository = userEntityRepository;
    }

    public void sendUserId(User user) {
        kafkaTemplate.send("user-topics", user);  // "user-topic"은 Kafka 토픽 이름
    }

    public GetUserDataResponseDto getUser(String userId) {
        UserEntity user = userEntityRepository.getReferenceById(userId);
        return GetUserDataResponseDto.builder()
                .name(user.getName())
                .photo(user.getPhoto())
                .build();
    }
}