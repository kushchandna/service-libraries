package com.kush.messaging_old.ordering;

import java.time.LocalDateTime;
import java.util.Comparator;

import com.kush.messaging_old.message.Message;

public class RecentFirst implements Comparator<Message> {

    public static final Comparator<Message> INSTANCE = new RecentFirst();

    private RecentFirst() {
    }

    @Override
    public int compare(Message o1, Message o2) {
        LocalDateTime sentTime1 = o1.getMetadata().getSentTime();
        LocalDateTime sentTime2 = o2.getMetadata().getSentTime();
        return sentTime1.isAfter(sentTime2) ? 1 : -1;
    }
}
