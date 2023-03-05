package com.kush.messaging_old.message;

import java.time.LocalDateTime;

import com.kush.commons.id.Identifier;

public interface MessagesUpdate {
	
	Identifier getGroupId();
	
	Message getMessage();
	
	LocalDateTime getMessageTime();
}
