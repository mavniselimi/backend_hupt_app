package com.hupt.hupt_backend.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "event_registrations",
        uniqueConstraints = {
                // A user can only register once per event
                @UniqueConstraint(columnNames = {"event_id", "user_id"}),
                // Safety net for concurrency: no two users can get the same queue number at the same desk
                @UniqueConstraint(columnNames = {"event_id", "assigned_registrar_id", "queue_number"})
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

    // The desk / registrar this user was routed to during registration
    // Assigned automatically by the load-balancing logic (least-loaded active desk)
    @ManyToOne
    @JoinColumn(name = "assigned_registrar_id")
    private User assignedRegistrar;

    // Queue position within the assigned registrar's own queue (not global)
    // e.g. Desk A: 1, 2, 3 — Desk B: 1, 2 — these are independent counters
    private Integer queueNumber;

    @Enumerated(EnumType.STRING)
    private EventRegistrationStatus status;

    // Who actually issued the card at the desk (set when status → CARD_ISSUED)
    // Usually the same as assignedRegistrar, but could differ if someone else stepped in
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

    public User getAssignedRegistrar() { return assignedRegistrar; }
    public void setAssignedRegistrar(User assignedRegistrar) { this.assignedRegistrar = assignedRegistrar; }

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
