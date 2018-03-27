package com.kush.messaging.metadata;

import java.util.HashMap;
import java.util.Map;

public class MapBasedMetadata implements Metadata {

    private final Map<String, Object> properties;

    public MapBasedMetadata(Map<String, Object> properties) {
        this.properties = new HashMap<>(properties);
    }

    @Override
    public <T> T getValue(String key, Class<T> type) {
        return type.cast(properties.get(key));
    }
}
