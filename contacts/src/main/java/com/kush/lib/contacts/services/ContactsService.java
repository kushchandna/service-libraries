package com.kush.lib.contacts.services;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import com.kush.lib.contacts.entities.Contact;
import com.kush.lib.contacts.persistors.ContactsPersistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.service.server.BaseService;
import com.kush.lib.service.server.annotations.Service;
import com.kush.lib.service.server.annotations.ServiceMethod;
import com.kush.lib.service.server.authentication.AuthenticationRequired;
import com.kush.utils.exceptions.ValidationFailedException;
import com.kush.utils.id.Identifier;

@Service
public class ContactsService extends BaseService {

    @AuthenticationRequired
    @ServiceMethod
    public Contact addToContacts(Identifier userId) throws ValidationFailedException, PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        if (currentUserId.equals(userId)) {
            throw new ValidationFailedException("Can not add self to contacts");
        }
        ContactsPersistor persistor = getInstance(ContactsPersistor.class);
        Clock clock = getInstance(Clock.class);
        LocalDateTime dateTime = LocalDateTime.now(clock);
        return persistor.addContact(currentUserId, userId, dateTime);
    }

    @AuthenticationRequired
    @ServiceMethod
    public List<Contact> getContacts() throws PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        ContactsPersistor persistor = getInstance(ContactsPersistor.class);
        return persistor.getContacts(currentUserId);
    }
}
