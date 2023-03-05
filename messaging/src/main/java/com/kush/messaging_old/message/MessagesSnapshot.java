package com.kush.messaging_old.message;

import java.time.LocalDateTime;
import java.util.Collection;

public interface MessagesSnapshot {

	Collection<MessagesUpdate> getMessageUpdates();
	
	LocalDateTime getLatestMessageTime();
}
