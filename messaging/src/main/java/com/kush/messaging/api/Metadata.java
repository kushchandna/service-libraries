package com.kush.messaging.api;

import java.io.Serializable;
import java.time.ZonedDateTime;

import com.kush.commons.id.Identifier;
import com.kush.service.annotations.Exportable;

@Exportable
public class Metadata implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Identifier sender;
    private final ZonedDateTime serverSentTime;
    private final ClientMetadata clientMetadata;

    public Metadata(Identifier sender, ZonedDateTime serverSentTime, ClientMetadata clientMetadata) {
        this.sender = sender;
        this.serverSentTime = serverSentTime;
		this.clientMetadata = clientMetadata;
    }

    public Identifier getSender() {
        return sender;
    }

    public ZonedDateTime getServerSentTime() {
        return serverSentTime;
    }
    
    public ClientMetadata getClientMetadata() {
		return clientMetadata;
	}
}
