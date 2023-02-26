package com.kush.lib.contacts.persistors;

import java.time.LocalDateTime;
import java.util.List;

import com.kush.commons.id.Identifiable;
import com.kush.commons.id.Identifier;
import com.kush.lib.contacts.entities.Contact;
import com.kush.lib.persistence.api.Persister;
import com.kush.lib.persistence.api.PersistenceOperationFailedException;

public interface ContactsPersister extends Persister<Contact> {

    Contact addContact(Identifier ownerUserId, Identifiable contactObject, LocalDateTime contactAddTime)
            throws PersistenceOperationFailedException;

    void removeContact(Identifier currentUserId, Identifiable contactObject) throws PersistenceOperationFailedException;

    List<Contact> getContacts(Identifier ownerUserId) throws PersistenceOperationFailedException;

    Contact getContact(Identifier ownerUserId, Identifiable contactObject) throws PersistenceOperationFailedException;
}
