package com.hillel.artemjev.phonebook.services.contacts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hillel.artemjev.phonebook.dto.StatusResponse;
import com.hillel.artemjev.phonebook.dto.contacts.*;
import com.hillel.artemjev.phonebook.entities.Contact;
import com.hillel.artemjev.phonebook.services.user.UserService;
import com.hillel.artemjev.phonebook.util.ContactDtoBuilder;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class ApiContactsService implements ContactsService {
    private final UserService userService;
    private final String baseUri;
    private final ContactDtoBuilder contactDtoBuilder;
    private final ObjectMapper mapper;
    private final HttpClient httpClient;

    @Override
    public void add(Contact contact) {
        AddContactRequest request = contactDtoBuilder.getAddContactRequest(contact);
        try {
            HttpRequest httpRequest = createAuthorizedPostRequest("/contacts/add", request);
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            StatusResponse statusResponse = mapper.readValue(response.body(), StatusResponse.class);
            if (!statusResponse.isSuccess()) {
                throw new RuntimeException("contact is not added" + statusResponse.getError());
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Contact> getAll() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUri + "/contacts"))
                .header("Authorization", "Bearer " + userService.getToken())
                .header("Accept", "application/json")
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            GetContactsResponse getContactsResponse = mapper.readValue(response.body(), GetContactsResponse.class);
            if (!getContactsResponse.isSuccess()) {
                throw new RuntimeException("no contacts received" + getContactsResponse.getError());
            }
            return getContactsResponse.getContacts().stream()
                    .map(c -> contactDtoBuilder.getContact(c))
                    .collect(Collectors.toList());
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public List<Contact> findByName(String name) {
        FindByNameContactRequest request = contactDtoBuilder.getFindByNameContactRequest(name);
        try {
            HttpRequest httpRequest = createAuthorizedPostRequest("/contacts/find", request);
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            PostContactsResponse postContactsResponse = mapper.readValue(response.body(), PostContactsResponse.class);
            if (!postContactsResponse.isSuccess()) {
                throw new RuntimeException("contacts are not found" + postContactsResponse.getError());
            }
            return postContactsResponse.getContacts().stream()
                    .map(c -> contactDtoBuilder.getContact(c))
                    .collect(Collectors.toList());
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public List<Contact> findByValue(String value) {
        FindByValueContactRequest request = contactDtoBuilder.getFindByValueContactRequest(value);
        try {
            HttpRequest httpRequest = createAuthorizedPostRequest("/contacts/find", request);
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            PostContactsResponse postContactsResponse = mapper.readValue(response.body(), PostContactsResponse.class);
            if (!postContactsResponse.isSuccess()) {
                throw new RuntimeException("contacts are not found" + postContactsResponse.getError());
            }
            return postContactsResponse.getContacts().stream()
                    .map(c -> contactDtoBuilder.getContact(c))
                    .collect(Collectors.toList());
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isAuth() {
        return this.userService.isAuth();
    }

    @Override
    public void remove(Integer id) {
        System.out.println("removed");
    }

    //------------------------------------------------------------------
    private HttpRequest createAuthorizedPostRequest(String uri, Object request) throws JsonProcessingException {
        return HttpRequest.newBuilder()
                .uri(URI.create(baseUri + uri))
                .header("Authorization", "Bearer " + userService.getToken())
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(request)))
                .build();
    }
}
