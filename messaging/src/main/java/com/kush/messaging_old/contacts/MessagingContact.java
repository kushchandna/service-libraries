package com.kush.messaging_old.contacts;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.kush.lib.contacts.entities.Contact;
import com.kush.messaging_old.message.Message;
import com.kush.service.annotations.Exportable;

@Exportable
public class MessagingContact implements Serializable, Comparable<MessagingContact> {

    private static final long serialVersionUID = 1L;

    private final Contact contact;
    private final List<Message> recentMessages;

    public MessagingContact(Contact contact, List<Message> recentMessages) {
        Objects.requireNonNull(recentMessages, "recentMessages");
        this.contact = contact;
        this.recentMessages = recentMessages;
    }

    public Contact getContact() {
        return contact;
    }

    public List<Message> getRecentMessages() {
        return recentMessages;
    }

    private Message getLastMessage() {
        return recentMessages.isEmpty() ? null : recentMessages.get(recentMessages.size() - 1);
    }

    @Override
    public int compareTo(MessagingContact other) {
        if (other == null) {
            return -1;
        }
        Message thisLastMessage = getLastMessage();
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
