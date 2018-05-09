package com.kush.lib.contacts;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.kush.lib.contacts.entities.Contact;
import com.kush.lib.contacts.persistors.ContactsPersistor;
import com.kush.lib.contacts.persistors.DefaultContactsPersistor;
import com.kush.lib.contacts.services.ContactsService;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.helpers.InMemoryPersistor;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.server.BaseServiceTest;
import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class ContactsServiceTest extends BaseServiceTest {

    private ContactsService contactsService;

    @Before
    public void beforeEachTest() throws Exception {
        contactsService = new ContactsService();
        registerService(contactsService);

        Persistor<Contact> contactsPersistor = InMemoryPersistor.forType(Contact.class);
        addToContext(ContactsPersistor.class, new DefaultContactsPersistor(contactsPersistor));
    }

    @Test
    public void contact() throws Exception {
        User user1 = getUser(0);
        User user2 = getUser(1);
        User user3 = getUser(2);

        runAuthenticatedOperation(user1, () -> {
            contactsService.addToContacts(user2);
            contactsService.addToContacts(user3);

            List<Contact> contacts = contactsService.getContacts();

            List<Identifier> owners = contacts.stream().map(c -> c.getOwnerUserId()).collect(toList());
            assertThat(owners, contains(user1.getId(), user1.getId()));

            List<Identifiable> contactObjects = contacts.stream().map(c -> c.getContactObject()).collect(toList());
            assertThat(contactObjects, contains(user2, user3));
        });
    }

    @Test
    public void getContact() throws Exception {
        User self = getUser(0);
        User other = getUser(1);

        runAuthenticatedOperation(self, () -> {
            Contact addedContact = contactsService.addToContacts(other);
            verifyContact(addedContact, self, other);
            List<Contact> contacts = contactsService.getContacts();
            verifyContact(contacts.get(0), self, other);
            Contact savedContact = contactsService.getContact(other);
            verifyContact(savedContact, self, other);
        });
    }

    private void verifyContact(Contact actualContact, User ownerUser, User contactObject) {
        assertThat(actualContact.getId(), is(notNullValue()));
        assertThat(actualContact.getOwnerUserId(), is(equalTo(ownerUser.getId())));
        assertThat(actualContact.getContactObject(), is(equalTo(contactObject)));
        assertThat(actualContact.getContactAddTime(), is(equalTo(LocalDateTime.now(CLOCK))));
    }
}
