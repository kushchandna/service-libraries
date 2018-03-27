package com.kush.messaging.metadata;

public interface Metadata {

    <T> T getValue(String key, Class<T> type);
}
