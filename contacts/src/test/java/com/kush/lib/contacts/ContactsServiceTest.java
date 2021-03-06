package com.kush.lib.contacts;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.kush.commons.id.Identifiable;
import com.kush.lib.contacts.entities.Contact;
import com.kush.lib.contacts.persistors.ContactsPersister;
import com.kush.lib.contacts.persistors.DefaultContactsPersistor;
import com.kush.lib.contacts.services.ContactsService;
import com.kush.lib.persistence.api.Persister;
import com.kush.lib.persistence.helpers.InMemoryPersister;
import com.kush.service.BaseServiceTest;

public class ContactsServiceTest extends BaseServiceTest {

    private ContactsService contactsService;

    @Before
    public void beforeEachTest() throws Exception {
        Persister<Contact> contactsPersistor = InMemoryPersister.forType(Contact.class);
        addToContext(ContactsPersister.class, new DefaultContactsPersistor(contactsPersistor));
        contactsService = registerService(ContactsService.class);
    }

    @Test
    public void addRemoveContacts() throws Exception {
        runAuthenticatedOperation(user(0), () -> {
            contactsService.addToContacts(user(1));
            contactsService.addToContacts(user(2));
            verifyContacts(contactsService.getContacts(), user(0), user(1), user(2));

            contactsService.removeFromContacts(user(1));
            verifyContacts(contactsService.getContacts(), user(0), user(2));
        });
    }

    @Test
    public void getContact() throws Exception {
        runAuthenticatedOperation(user(0), () -> {
            Contact addedContact = contactsService.addToContacts(user(1));
            verifyContact(addedContact, user(0), user(1));
            List<Contact> contacts = contactsService.getContacts();
            verifyContacts(contacts, user(0), user(1));
            Contact savedContact = contactsService.getContact(user(1));
            verifyContact(savedContact, user(0), user(1));
        });
    }

    private void verifyContacts(List<Contact> actualContacts, Identifiable owner, Identifiable... expectedContactObjects) {
        assertThat(actualContacts, hasSize(expectedContactObjects.length));
        for (int i = 0; i < expectedContactObjects.length; i++) {
            Identifiable expectedContactObject = expectedContactObjects[i];
            verifyContact(actualContacts.get(i), owner, expectedContactObject);
        }
    }

    private void verifyContact(Contact actualContact, Identifiable ownerUser, Identifiable contactObject) {
        assertThat(actualContact.getId(), is(notNullValue()));
        assertThat(actualContact.getOwnerUserId(), is(equalTo(ownerUser.getId())));
        assertThat(actualContact.getContactObject(), is(equalTo(contactObject)));
        assertThat(actualContact.getContactAddTime(), is(equalTo(LocalDateTime.now(CLOCK))));
    }
}
