package com.kush.lib.contacts.persistors;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import com.kush.lib.contacts.entities.Contact;
import com.kush.lib.persistence.api.DelegatingPersistor;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class DefaultContactsPersistor extends DelegatingPersistor<Contact> implements ContactsPersistor {

    public DefaultContactsPersistor(Persistor<Contact> delegate) {
        super(delegate);
    }

    @Override
    public Contact addContact(Identifier ownerUserId, Identifiable contactObject, LocalDateTime contactAddTime)
            throws PersistorOperationFailedException {
        Contact existing = getMatchingContact(ownerUserId, contactObject);
        if (existing != null) {
            throw new PersistorOperationFailedException("Already exists");
        }
        return save(new Contact(ownerUserId, contactObject, contactAddTime));
    }

    @Override
    public void removeContact(Identifier ownerUserId, Identifiable contactObject) throws PersistorOperationFailedException {
        Contact existingContact = getExistingContact(ownerUserId, contactObject);
        remove(existingContact.getId());
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

    @Override
    public Contact getContact(Identifier ownerUserId, Identifiable contactObject) throws PersistorOperationFailedException {
        return getExistingContact(ownerUserId, contactObject);
    }

    private Contact getExistingContact(Identifier ownerUserId, Identifiable contactObject)
            throws PersistorOperationFailedException {
        Contact existingContact = getMatchingContact(ownerUserId, contactObject);
        if (existingContact == null) {
            throw new PersistorOperationFailedException("No such contact found");
        }
        return existingContact;
    }

    private Contact getMatchingContact(Identifier ownerUserId, Identifiable contactObject)
            throws PersistorOperationFailedException {
        List<Contact> matchingContacts =
                fetch(c -> c.getOwnerUserId().equals(ownerUserId) && c.getContactObject().equals(contactObject));
        if (matchingContacts.isEmpty()) {
            return null;
        }
        if (matchingContacts.size() > 1) {
            throw new PersistorOperationFailedException("More than one matching contacts found");
        }
        return matchingContacts.get(0);
    }
}
