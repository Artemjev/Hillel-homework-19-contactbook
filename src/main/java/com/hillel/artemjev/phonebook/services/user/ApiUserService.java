package com.hillel.artemjev.phonebook.services.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hillel.artemjev.phonebook.dto.user.LoginRequest;
import com.hillel.artemjev.phonebook.dto.user.LoginResponse;
import com.hillel.artemjev.phonebook.dto.user.RegisterRequest;
import com.hillel.artemjev.phonebook.dto.user.GetUserResponse;
import com.hillel.artemjev.phonebook.entities.User;
import com.hillel.artemjev.phonebook.dto.*;
import com.hillel.artemjev.phonebook.services.AccessToken;
import com.hillel.artemjev.phonebook.util.UserDtoBuilder;
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
public class ApiUserService implements UserService {
    private final String baseUri;
    private final UserDtoBuilder userDtoBuilder;
    final private AccessToken token;
    private final ObjectMapper mapper;
    private final HttpClient httpClient;

    @Override
    public boolean isAuth() {
        return this.token.isValid();
    }

    @Override
    public void register(User user) {
        RegisterRequest registerRequest = userDtoBuilder.getRegisterRequest(user);
        try {
            HttpRequest request = createPostRequest("/register", registerRequest);
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            StatusResponse statusResponse = mapper.readValue(response.body(), StatusResponse.class);
            if (!statusResponse.isSuccess()) {
                throw new RuntimeException("authorization failed" + statusResponse.getError());
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void login(User user) {
        LoginRequest loginRequest = userDtoBuilder.getLoinRequest(user);
        try {
            HttpRequest request = createPostRequest("/login", loginRequest);
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            LoginResponse loginResponse = mapper.readValue(response.body(), LoginResponse.class);
            if (!loginResponse.isSuccess()) {
                throw new RuntimeException("authorization failed" + loginResponse.getError());
            }
            token.refreshToken(loginResponse.getToken());
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAll() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://mag-contacts-api.herokuapp.com/users"))
                .GET()
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            GetUserResponse getUserResponse = mapper.readValue(response.body(), GetUserResponse.class);

            if (!getUserResponse.isSuccess()) {
                throw new RuntimeException("no users received" + getUserResponse.getError());
            }
            return getUserResponse.getUsers().stream()
                    .map(u -> userDtoBuilder.getUser(u))
                    .collect(Collectors.toList());
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public String getToken() {
        return token.getToken();
    }

    //------------------------------------------------------------------
    private HttpRequest createPostRequest(String uri, Object request) throws JsonProcessingException {
        return HttpRequest.newBuilder()
                .uri(URI.create(baseUri + uri))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(request)))
                .build();
    }

    private void refreshToken(String token) {
        this.token.refreshToken(token);
    }
}
