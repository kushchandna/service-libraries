package com.kush.messaging.core;

import java.io.Serializable;

import com.kush.messaging.content.Content;
import com.kush.messaging.metadata.Metadata;
import com.kush.utils.id.Identifiable;

public interface Message extends Identifiable, Serializable {

    Content getContent();

    Metadata getMetadata();
}
