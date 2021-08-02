package com.kush.lib.contacts.services;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import com.kush.commons.id.Identifiable;
import com.kush.commons.id.Identifier;
import com.kush.lib.contacts.entities.Contact;
import com.kush.lib.contacts.persistors.ContactsPersister;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.service.remoting.auth.User;
import com.kush.service.BaseService;
import com.kush.service.annotations.Service;
import com.kush.service.annotations.ServiceMethod;
import com.kush.service.auth.AuthenticationRequired;
import com.kush.utils.exceptions.ValidationFailedException;

@Service
public class ContactsService extends BaseService {

    @AuthenticationRequired
    @ServiceMethod
    public Contact addToContacts(Identifiable contactObject) throws ValidationFailedException, PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        if (contactObject instanceof User && currentUserId.equals(contactObject.getId())) {
            throw new ValidationFailedException("Can not add self to contacts");
        }
        ContactsPersister persistor = getInstance(ContactsPersister.class);
        Clock clock = getInstance(Clock.class);
        LocalDateTime dateTime = LocalDateTime.now(clock);
        return persistor.addContact(currentUserId, contactObject, dateTime);
    }

    @AuthenticationRequired
    @ServiceMethod
    public void removeFromContacts(Identifiable contactObject) throws PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        ContactsPersister persistor = getInstance(ContactsPersister.class);
        persistor.removeContact(currentUserId, contactObject);
    }

    @AuthenticationRequired
    @ServiceMethod
    public List<Contact> getContacts() throws PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        ContactsPersister persistor = getInstance(ContactsPersister.class);
        return persistor.getContacts(currentUserId);
    }

    @AuthenticationRequired
    @ServiceMethod
    public Contact getContact(Identifiable contactObject) throws PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        ContactsPersister persistor = getInstance(ContactsPersister.class);
        return persistor.getContact(currentUserId, contactObject);
    }

    @Override
    protected void processContext() {
        checkContextHasValueFor(ContactsPersister.class);
        addIfDoesNotExist(Clock.class, Clock.systemUTC());
    }
}
