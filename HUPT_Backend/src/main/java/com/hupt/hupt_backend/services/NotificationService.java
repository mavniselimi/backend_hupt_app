package com.hupt.hupt_backend.services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.hupt.hupt_backend.entities.DeviceToken;
import com.hupt.hupt_backend.repositories.DeviceTokenRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final DeviceTokenRepository deviceTokenRepository;

    public NotificationService(DeviceTokenRepository deviceTokenRepository) {
        this.deviceTokenRepository = deviceTokenRepository;
    }

    // 🔹 generic method
    public void sendToUser(Long userId, String title, String body) throws Exception {
        List<String> tokens = deviceTokenRepository.findByUserId(userId)
                .stream()
                .map(DeviceToken::getToken)
                .distinct()
                .toList();

        if (tokens.isEmpty()) return;

        MulticastMessage message = MulticastMessage.builder()
                .addAllTokens(tokens)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        FirebaseMessaging.getInstance().sendEachForMulticast(message);
    }

    // 🔥 SENİN SORDUĞUN METHOD
    public void sendSessionStartedToUser(Long userId, Long eventId, Long sessionId, String sessionTitle) throws Exception {

        List<String> tokens = deviceTokenRepository.findByUserId(userId)
                .stream()
                .map(DeviceToken::getToken)
                .distinct()
                .toList();

        if (tokens.isEmpty()) return;

        MulticastMessage message = MulticastMessage.builder()
                .addAllTokens(tokens)
                .setNotification(Notification.builder()
                        .setTitle("Oturum Başladı")
                        .setBody(sessionTitle + " şu anda aktif.")
                        .build())
                .putData("type", "SESSION_STARTED")
                .putData("eventId", String.valueOf(eventId))
                .putData("sessionId", String.valueOf(sessionId))
                .build();

        FirebaseMessaging.getInstance().sendEachForMulticast(message);
    }
}