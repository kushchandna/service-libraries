package com.kush.lib.contacts.persistors;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import com.kush.commons.id.Identifiable;
import com.kush.commons.id.Identifier;
import com.kush.lib.contacts.entities.Contact;
import com.kush.lib.persistence.api.DelegatingPersister;
import com.kush.lib.persistence.api.Persister;
import com.kush.lib.persistence.api.PersistenceOperationFailedException;

public class DefaultContactsPersistor extends DelegatingPersister<Contact> implements ContactsPersister {

    public DefaultContactsPersistor(Persister<Contact> delegate) {
        super(delegate);
    }

    @Override
    public Contact addContact(Identifier ownerUserId, Identifiable contactObject, LocalDateTime contactAddTime)
            throws PersistenceOperationFailedException {
        Contact existing = getMatchingContact(ownerUserId, contactObject);
        if (existing != null) {
            throw new PersistenceOperationFailedException("Already exists");
        }
        return save(new Contact(ownerUserId, contactObject, contactAddTime));
    }

    @Override
    public void removeContact(Identifier ownerUserId, Identifiable contactObject) throws PersistenceOperationFailedException {
        Contact existingContact = getExistingContact(ownerUserId, contactObject);
        remove(existingContact.getId());
    }

    @Override
    public List<Contact> getContacts(Identifier ownerUserId) throws PersistenceOperationFailedException {
        return fetch(c -> c.getOwnerUserId().equals(ownerUserId), new Comparator<Contact>() {

            @Override
            public int compare(Contact o1, Contact o2) {
                return o1.getContactAddTime().compareTo(o2.getContactAddTime());
            }
        }, -1);
    }

    @Override
    public Contact getContact(Identifier ownerUserId, Identifiable contactObject) throws PersistenceOperationFailedException {
        return getExistingContact(ownerUserId, contactObject);
    }

    private Contact getExistingContact(Identifier ownerUserId, Identifiable contactObject)
            throws PersistenceOperationFailedException {
        Contact existingContact = getMatchingContact(ownerUserId, contactObject);
        if (existingContact == null) {
            throw new PersistenceOperationFailedException("No such contact found");
        }
        return existingContact;
    }

    private Contact getMatchingContact(Identifier ownerUserId, Identifiable contactObject)
            throws PersistenceOperationFailedException {
        List<Contact> matchingContacts =
                fetch(c -> c.getOwnerUserId().equals(ownerUserId) && c.getContactObject().equals(contactObject));
        if (matchingContacts.isEmpty()) {
            return null;
        }
        if (matchingContacts.size() > 1) {
            throw new PersistenceOperationFailedException("More than one matching contacts found");
        }
        return matchingContacts.get(0);
    }
}
