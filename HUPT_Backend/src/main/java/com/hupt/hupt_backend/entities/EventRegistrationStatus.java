package com.hupt.hupt_backend.entities;

public enum EventRegistrationStatus {
    PENDING,     // registered, waiting to be called to the physical desk
    CARD_ISSUED  // the registrar wrote their name on the card and handed it over
}
