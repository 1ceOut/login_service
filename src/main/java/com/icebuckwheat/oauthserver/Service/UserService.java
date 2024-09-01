package com.icebuckwheat.oauthserver.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icebuckwheat.oauthserver.Dto.AddinfoRequsetDto;
import com.icebuckwheat.oauthserver.Dto.GetUserDataResponseDto;
import com.icebuckwheat.oauthserver.Dto.User;
import com.icebuckwheat.oauthserver.Dto.UserEntityDto;
import com.icebuckwheat.oauthserver.Entity.UserEntity;
import com.icebuckwheat.oauthserver.Repository.UserEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, User> kafkaTemplate;
    private final UserEntityRepository userEntityRepository;

    @Autowired
    public UserService(ObjectMapper objectMapper, KafkaTemplate<String, User> kafkaTemplate, UserEntityRepository userEntityRepository) {
        this.objectMapper = objectMapper;
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

    public List<UserEntityDto> getAllUsers() {
        return userEntityRepository.findAll().stream().map(UserEntity::toDto).collect(Collectors.toList());
    }

    @Transactional
    public boolean addInfo(AddinfoRequsetDto dto) {
        try {
            UserEntity entity = userEntityRepository.findById(dto.getUserId()).get();
            String buf = "";
            for (Object item : dto.getFavorite()) {
                buf += ((Map<String, String>) item).get("value") + ",";
            }

            entity.setPreference(buf);
            entity.setRole("ROLE_USER");
            entity.setHeight(dto.getHeight());
            entity.setWeight(dto.getWeight());
            userEntityRepository.save(entity);
        } catch (Exception ignored) {
            return false;
        }
        return true;
    }
}