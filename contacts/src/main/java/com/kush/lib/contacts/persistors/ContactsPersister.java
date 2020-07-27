package com.kush.lib.contacts.persistors;

import java.time.LocalDateTime;
import java.util.List;

import com.kush.lib.contacts.entities.Contact;
import com.kush.lib.persistence.api.Persister;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public interface ContactsPersister extends Persister<Contact> {

    Contact addContact(Identifier ownerUserId, Identifiable contactObject, LocalDateTime contactAddTime)
            throws PersistorOperationFailedException;

    void removeContact(Identifier currentUserId, Identifiable contactObject) throws PersistorOperationFailedException;

    List<Contact> getContacts(Identifier ownerUserId) throws PersistorOperationFailedException;

    Contact getContact(Identifier ownerUserId, Identifiable contactObject) throws PersistorOperationFailedException;
}
