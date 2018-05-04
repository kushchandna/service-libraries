package com.kush.lib.contacts.entities;

import java.time.LocalDateTime;

import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class Contact implements Identifiable {

    private final Identifier contactId;
    private final Identifier ownerUserId;
    private final Identifier contactUserId;
    private final LocalDateTime contactAddTime;

    public Contact(Identifier ownerUserId, Identifier contactUserId, LocalDateTime contactAddTime) {
        this(Identifier.NULL, ownerUserId, contactUserId, contactAddTime);
    }

    public Contact(Identifier contactId, Contact subscription) {
        this(contactId, subscription.getOwnerUserId(), subscription.getContactUserId(), subscription.getContactAddTime());
    }

    public Contact(Identifier contactId, Identifier ownerUserId, Identifier contactUserId, LocalDateTime contactAddTime) {
        this.contactId = contactId;
        this.ownerUserId = ownerUserId;
        this.contactUserId = contactUserId;
        this.contactAddTime = contactAddTime;
    }

    @Override
    public Identifier getId() {
        return contactId;
    }

    public Identifier getOwnerUserId() {
        return ownerUserId;
    }

    public Identifier getContactUserId() {
        return contactUserId;
    }

    public LocalDateTime getContactAddTime() {
        return contactAddTime;
    }
}
