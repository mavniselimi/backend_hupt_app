package com.hupt.hupt_backend.services;

import com.hupt.hupt_backend.dto.DeviceTokenRegisterRequestDto;
import com.hupt.hupt_backend.entities.DeviceToken;
import com.hupt.hupt_backend.entities.User;
import com.hupt.hupt_backend.repositories.DeviceTokenRepository;
import com.hupt.hupt_backend.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeviceTokenService {

    private final DeviceTokenRepository deviceTokenRepository;
    private final UserRepository userRepository;

    public DeviceTokenService(DeviceTokenRepository deviceTokenRepository,
                              UserRepository userRepository) {
        this.deviceTokenRepository = deviceTokenRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public DeviceToken registerToken(Long userId, DeviceTokenRegisterRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        String normalizedToken = request.getToken().trim();

        return deviceTokenRepository.findByToken(normalizedToken)
                .map(existing -> {
                    existing.setUser(user);
                    existing.setPlatform(request.getPlatform());
                    return deviceTokenRepository.save(existing);
                })
                .orElseGet(() -> {
                    DeviceToken deviceToken = new DeviceToken();
                    deviceToken.setUser(user);
                    deviceToken.setPlatform(request.getPlatform());
                    deviceToken.setToken(normalizedToken);
                    return deviceTokenRepository.save(deviceToken);
                });
    }

    public List<DeviceToken> getMyTokens(Long userId) {
        return deviceTokenRepository.findByUserId(userId);
    }

    @Transactional
    public void deleteMyToken(Long userId, String token) {
        DeviceToken deviceToken = deviceTokenRepository.findByToken(token.trim())
                .orElseThrow(() -> new RuntimeException("Token not found"));

        if (!deviceToken.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to delete this token");
        }

        deviceTokenRepository.delete(deviceToken);
    }

    @Transactional
    public void deleteAllMyTokensByPlatform(Long userId, String platformRaw) {
        deviceTokenRepository.deleteByUserIdAndPlatform(
                userId,
                Enum.valueOf(com.hupt.hupt_backend.entities.DevicePlatform.class, platformRaw.toUpperCase())
        );
    }
}