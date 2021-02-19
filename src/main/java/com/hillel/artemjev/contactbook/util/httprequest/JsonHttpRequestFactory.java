package com.hillel.artemjev.contactbook.util.httprequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.net.http.HttpRequest;

@RequiredArgsConstructor
public class JsonHttpRequestFactory implements HttpRequestFactory {
    private final ObjectMapper objectMapper;

    @Override
    public HttpRequest createGetRequest(String url) {
        return HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .build();
    }

    @Override
    public HttpRequest createPostRequest(String url, Object obj) {
        String objectStr = null;
        try {
            objectStr = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(objectStr))
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
    }

    @Override
    public HttpRequest createAuthorizedGetRequest(String url, String token) {
        return HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();
    }

    @Override
    public HttpRequest createAuthorizedPostRequest(String url, Object obj, String token) {
        String objectStr = null;
        try {
            objectStr = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(objectStr))
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
    }
}