package com.hupt.hupt_backend.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "event_registrations",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"event_id", "user_id"})
        }
)
public class EventRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    // Sequential number per event (1, 2, 3...) — the physical queue order
    private Integer queueNumber;

    @Enumerated(EnumType.STRING)
    private EventRegistrationStatus status;

    // Which registrar processed this person — null until card is issued
    @ManyToOne
    @JoinColumn(name = "processed_by_id")
    private User processedBy;

    private LocalDateTime registeredAt;
    private LocalDateTime cardIssuedAt;

    @PrePersist
    public void prePersist() {
        registeredAt = LocalDateTime.now();
        if (status == null) {
            status = EventRegistrationStatus.PENDING;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Integer getQueueNumber() { return queueNumber; }
    public void setQueueNumber(Integer queueNumber) { this.queueNumber = queueNumber; }

    public EventRegistrationStatus getStatus() { return status; }
    public void setStatus(EventRegistrationStatus status) { this.status = status; }

    public User getProcessedBy() { return processedBy; }
    public void setProcessedBy(User processedBy) { this.processedBy = processedBy; }

    public LocalDateTime getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(LocalDateTime registeredAt) { this.registeredAt = registeredAt; }

    public LocalDateTime getCardIssuedAt() { return cardIssuedAt; }
    public void setCardIssuedAt(LocalDateTime cardIssuedAt) { this.cardIssuedAt = cardIssuedAt; }
}
