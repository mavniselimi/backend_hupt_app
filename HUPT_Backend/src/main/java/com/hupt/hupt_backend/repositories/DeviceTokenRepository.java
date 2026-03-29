package com.hupt.hupt_backend.repositories;

import com.hupt.hupt_backend.entities.DevicePlatform;
import com.hupt.hupt_backend.entities.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {

    Optional<DeviceToken> findByToken(String token);

    List<DeviceToken> findByUserId(Long userId);

    List<DeviceToken> findByUserIdAndPlatform(Long userId, DevicePlatform platform);

    void deleteByToken(String token);

    void deleteByUserIdAndPlatform(Long userId, DevicePlatform platform);
}