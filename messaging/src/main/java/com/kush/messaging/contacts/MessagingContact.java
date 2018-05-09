package com.kush.messaging.contacts;

import java.io.Serializable;

import com.kush.lib.contacts.entities.Contact;

public class MessagingContact implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Contact contact;

    public MessagingContact(Contact contact) {
        this.contact = contact;
    }

    public Contact getContact() {
        return contact;
    }
}
