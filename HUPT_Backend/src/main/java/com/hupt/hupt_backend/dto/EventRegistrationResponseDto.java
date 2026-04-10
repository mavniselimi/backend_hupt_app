package com.hupt.hupt_backend.dto;

import java.time.LocalDateTime;

public class EventRegistrationResponseDto {

    private Long id;

    // Event info
    private Long eventId;
    private String eventTitle;

    // Registered user
    private Long userId;
    private String userName;
    private String userEmail;

    // Desk routing — the key info the frontend needs to say "Go to desk X, queue #Y"
    private Long assignedRegistrarId;
    private String assignedRegistrarName;   // Use registrar's name as the desk label (e.g. "Desk A", "Masa 1")
    private Integer queueNumber;            // Position in that desk's own queue (not global)

    // Status
    private String status;                  // PENDING / CARD_ISSUED

    // Who actually issued the card (set when CARD_ISSUED, usually == assignedRegistrar)
    private Long processedById;
    private String processedByName;

    // Timestamps
    private LocalDateTime registeredAt;
    private LocalDateTime cardIssuedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public String getEventTitle() { return eventTitle; }
    public void setEventTitle(String eventTitle) { this.eventTitle = eventTitle; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public Long getAssignedRegistrarId() { return assignedRegistrarId; }
    public void setAssignedRegistrarId(Long assignedRegistrarId) { this.assignedRegistrarId = assignedRegistrarId; }

    public String getAssignedRegistrarName() { return assignedRegistrarName; }
    public void setAssignedRegistrarName(String assignedRegistrarName) { this.assignedRegistrarName = assignedRegistrarName; }

    public Integer getQueueNumber() { return queueNumber; }
    public void setQueueNumber(Integer queueNumber) { this.queueNumber = queueNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getProcessedById() { return processedById; }
    public void setProcessedById(Long processedById) { this.processedById = processedById; }

    public String getProcessedByName() { return processedByName; }
    public void setProcessedByName(String processedByName) { this.processedByName = processedByName; }

    public LocalDateTime getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(LocalDateTime registeredAt) { this.registeredAt = registeredAt; }

    public LocalDateTime getCardIssuedAt() { return cardIssuedAt; }
    public void setCardIssuedAt(LocalDateTime cardIssuedAt) { this.cardIssuedAt = cardIssuedAt; }
}
