package com.kush.lib.contacts.persistors;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import com.kush.lib.contacts.entities.Contact;
import com.kush.lib.persistence.api.DelegatingPersistor;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.utils.id.Identifier;

public class DefaultContactsPersistor extends DelegatingPersistor<Contact> implements ContactsPersistor {

    public DefaultContactsPersistor(Persistor<Contact> delegate) {
        super(delegate);
    }

    @Override
    public Contact addContact(Identifier ownerUserId, Identifier contactUserId, LocalDateTime contactAddTime)
            throws PersistorOperationFailedException {
        checkContactDoesNotExist(ownerUserId, contactUserId);
        return save(new Contact(ownerUserId, contactUserId, contactAddTime));
    }

    @Override
    public List<Contact> getContacts(Identifier ownerUserId) throws PersistorOperationFailedException {
        return fetch(c -> c.getOwnerUserId().equals(ownerUserId), new Comparator<Contact>() {

            @Override
            public int compare(Contact o1, Contact o2) {
                return o1.getContactAddTime().compareTo(o2.getContactAddTime());
            }
        }, -1);
    }

    private void checkContactDoesNotExist(Identifier ownerUserId, Identifier contactUserId)
            throws PersistorOperationFailedException {
        List<Contact> existing = fetch(c -> c.getOwnerUserId().equals(ownerUserId) && c.getContactUserId().equals(contactUserId));
        if (!existing.isEmpty()) {
            throw new PersistorOperationFailedException("Already exists");
        }
    }
}
