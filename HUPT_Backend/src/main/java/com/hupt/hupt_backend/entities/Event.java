package com.hupt.hupt_backend.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String description;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String location;

    private String pictureOfEventUrl;

    @ManyToOne
    private User createdBy;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Session> sessions = new ArrayList<>();

    // Replaced the old simple ManyToMany with a proper entity to track queue + card status
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventRegistration> registrations = new ArrayList<>();

    // Registrars (desks) assigned to manage physical registration for this event
    // Admin assigns these; only active ones receive new incoming registrations
    @ManyToMany
    @JoinTable(
            name = "event_registrar_assignments",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "registrar_id")
    )
    private List<User> assignedRegistrars = new ArrayList<>();

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist(){
        createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getPictureOfEventUrl() { return pictureOfEventUrl; }
    public void setPictureOfEventUrl(String pictureOfEventUrl) { this.pictureOfEventUrl = pictureOfEventUrl; }

    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }

    public List<Session> getSessions() { return sessions; }
    public void setSessions(List<Session> sessions) { this.sessions = sessions; }

    public List<EventRegistration> getRegistrations() { return registrations; }
    public void setRegistrations(List<EventRegistration> registrations) { this.registrations = registrations; }

    public List<User> getAssignedRegistrars() { return assignedRegistrars; }
    public void setAssignedRegistrars(List<User> assignedRegistrars) { this.assignedRegistrars = assignedRegistrars; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
