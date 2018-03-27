package com.kush.messaging.message;

import java.io.Serializable;

import com.kush.messaging.content.Content;
import com.kush.messaging.metadata.Metadata;

public interface Message extends Serializable {

    Content getContent();

    Metadata getMetadata();
}
