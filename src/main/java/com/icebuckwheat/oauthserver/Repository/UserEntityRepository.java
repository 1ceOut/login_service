package com.icebuckwheat.oauthserver.Repository;

import com.icebuckwheat.oauthserver.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntityRepository extends JpaRepository<UserEntity, String> {
    UserEntity findFirstByRefreshToken(String refreshToken);
}
