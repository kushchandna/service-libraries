package com.kush.lib.contacts.entities;

import java.time.LocalDateTime;

import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class Contact implements Identifiable {

    private final Identifier contactId;
    private final Identifier ownerUserId;
    private final Identifiable contactObject;
    private final LocalDateTime contactAddTime;

    public Contact(Identifier ownerUserId, Identifiable contactObject, LocalDateTime contactAddTime) {
        this(Identifier.NULL, ownerUserId, contactObject, contactAddTime);
    }

    public Contact(Identifier contactId, Contact contact) {
        this(contactId, contact.getOwnerUserId(), contact.getContactObject(), contact.getContactAddTime());
    }

    public Contact(Identifier contactId, Identifier ownerUserId, Identifiable contactObject, LocalDateTime contactAddTime) {
        this.contactId = contactId;
        this.ownerUserId = ownerUserId;
        this.contactObject = contactObject;
        this.contactAddTime = contactAddTime;
    }

    @Override
    public Identifier getId() {
        return contactId;
    }

    public Identifier getOwnerUserId() {
        return ownerUserId;
    }

    public Identifiable getContactObject() {
        return contactObject;
    }

    public LocalDateTime getContactAddTime() {
        return contactAddTime;
    }
}
