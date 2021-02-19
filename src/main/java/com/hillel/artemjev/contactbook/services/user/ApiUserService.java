package com.hillel.artemjev.contactbook.services.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hillel.artemjev.contactbook.dto.user.LoginRequest;
import com.hillel.artemjev.contactbook.dto.user.LoginResponse;
import com.hillel.artemjev.contactbook.dto.user.RegisterRequest;
import com.hillel.artemjev.contactbook.dto.user.GetUserResponse;
import com.hillel.artemjev.contactbook.entities.User;
import com.hillel.artemjev.contactbook.dto.*;
import com.hillel.artemjev.contactbook.services.AccessToken;
import com.hillel.artemjev.contactbook.util.UserDtoBuilder;
import com.hillel.artemjev.contactbook.util.httprequest.HttpRequestFactory;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
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
    private final HttpRequestFactory httpRequestFactory;
    private final HttpClient httpClient;

    @Override
    public boolean isAuth() {
        return this.token.isValid();
    }

    @Override
    public void register(User user) {
        RegisterRequest registerRequest = userDtoBuilder.getRegisterRequest(user);
        try {
            HttpRequest request = httpRequestFactory.createPostRequest(
                    baseUri + "/register",
                    registerRequest
            );
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
            HttpRequest request = httpRequestFactory.createPostRequest(
                    baseUri + "/login",
                    loginRequest
            );
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
        HttpRequest request = httpRequestFactory.createGetRequest(baseUri + "/users");
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
    private void refreshToken(String token) {
        this.token.refreshToken(token);
    }
}
