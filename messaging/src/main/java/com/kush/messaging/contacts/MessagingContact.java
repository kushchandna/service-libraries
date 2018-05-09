package com.kush.messaging.contacts;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.kush.lib.contacts.entities.Contact;
import com.kush.lib.service.server.annotations.Exportable;
import com.kush.messaging.message.Message;

@Exportable
public class MessagingContact implements Serializable, Comparable<MessagingContact> {

    private static final long serialVersionUID = 1L;

    private final Contact contact;
    private final Message lastMessage;

    public MessagingContact(Contact contact, Message lastMessage) {
        this.contact = contact;
        this.lastMessage = lastMessage;
    }

    public Contact getContact() {
        return contact;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    @Override
    public int compareTo(MessagingContact other) {
        if (other == null) {
            return -1;
        }
        Message thisLastMessage = this.getLastMessage();
        Message otherLastMessage = other.getLastMessage();
        if (otherLastMessage == null) {
            if (thisLastMessage == null) {
                return 0;
            } else {
                return -1;
            }
        }
        if (thisLastMessage == null) {
            return 1;
        }
        LocalDateTime thisLastMessageSentTime = thisLastMessage.getMetadata().getSentTime();
        LocalDateTime otherLastMessageSentTime = otherLastMessage.getMetadata().getSentTime();
        return otherLastMessageSentTime.compareTo(thisLastMessageSentTime);
    }
}
