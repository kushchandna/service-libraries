package com.kush.lib.location.google;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class GoogleApiConnection implements AutoCloseable {

    private static final String DEFAULT_ENCODING = "UTF-8";

    private final String apiKey;
    private final CloseableHttpClient client;

    public GoogleApiConnection(String apiKey) {
        this.apiKey = apiKey;
        client = HttpClientBuilder.create().build();
    }

    public String getResult(String apiUrl, Map<String, Object> params) throws IOException {
        String queryUrl = createQueryUrl(apiUrl, params);
        return executeQuery(queryUrl);
    }

    @Override
    public void close() throws Exception {
        client.close();
    }

    private String executeQuery(String queryUrl) throws IOException, ClientProtocolException {
        HttpGet request = new HttpGet(queryUrl);
        HttpResponse response = client.execute(request);
        InputStream responseStream = response.getEntity().getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
        return reader.lines().collect(Collectors.joining());
    }

    private String createQueryUrl(String apiUrl, Map<String, Object> params) throws IOException {
        Map<String, Object> paramsWithKey = populateParamsWithKey(params);
        String queryString = buildQueryString(paramsWithKey);
        return apiUrl + "?" + queryString;
    }

    private Map<String, Object> populateParamsWithKey(Map<String, Object> params) {
        Map<String, Object> paramsWithKey = new HashMap<>(params);
        paramsWithKey.put("key", apiKey);
        return paramsWithKey;
    }

    private static String buildQueryString(Map<String, Object> map) throws IOException {
        StringBuilder sb = new StringBuilder(map.size() * 8);
        Iterator<Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Object> entry = it.next();
            String key = entry.getKey();
            if (key == null) {
                throw new NullPointerException("Null key found in map");
            }
            sb.append(URLEncoder.encode(key, DEFAULT_ENCODING));
            sb.append("=");
            Object value = entry.getValue();
            String valueAsString = value != null ? URLEncoder.encode(value.toString(), DEFAULT_ENCODING) : "";
            sb.append(valueAsString);
            if (it.hasNext()) {
                sb.append("&");
            }
        }
        return sb.toString();
    }
}
