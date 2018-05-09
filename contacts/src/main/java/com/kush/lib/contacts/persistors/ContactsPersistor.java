package com.kush.lib.contacts.persistors;

import java.time.LocalDateTime;
import java.util.List;

import com.kush.lib.contacts.entities.Contact;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public interface ContactsPersistor extends Persistor<Contact> {

    Contact addContact(Identifier ownerUserId, Identifiable contactObject, LocalDateTime contactAddTime)
            throws PersistorOperationFailedException;

    List<Contact> getContacts(Identifier ownerUserId) throws PersistorOperationFailedException;

    Contact getContact(Identifier ownerUserId, Identifiable contactObject) throws PersistorOperationFailedException;
}