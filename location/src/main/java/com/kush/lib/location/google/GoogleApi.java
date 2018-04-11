package com.kush.lib.location.google;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

public class GoogleApi {

    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final String API_URL = "https://maps.googleapis.com/maps/api";

    public static String getUrlWithParameters(String apiName, Map<String, Object> parameters) {
        String queryString = buildQueryString(parameters);
        String queryUrl = API_URL + "/" + apiName;
        if (!queryString.isEmpty()) {
            queryUrl += ("?" + queryString);
        }
        return queryUrl;
    }

    private static String buildQueryString(final Map<String, Object> map) {
        try {
            final Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
            final StringBuilder sb = new StringBuilder(map.size() * 8);
            while (it.hasNext()) {
                final Map.Entry<String, Object> entry = it.next();
                final String key = entry.getKey();
                if (key != null) {
                    sb.append(URLEncoder.encode(key, DEFAULT_ENCODING));
                    sb.append("=");
                    final Object value = entry.getValue();
                    final String valueAsString = value != null ? URLEncoder.encode(value.toString(), DEFAULT_ENCODING) : "";
                    sb.append(valueAsString);
                    if (it.hasNext()) {
                        sb.append("&");
                    }
                } else {
                    assert false : String.format("Null key in query map: %s", map.entrySet());
                }
            }
            return sb.toString();
        } catch (final UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }
}
