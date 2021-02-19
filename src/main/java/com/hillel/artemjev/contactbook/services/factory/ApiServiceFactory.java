package com.hillel.artemjev.contactbook.services.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.hillel.artemjev.contactbook.config.AppProperties;
import com.hillel.artemjev.contactbook.services.AccessToken;
import com.hillel.artemjev.contactbook.services.contacts.ApiContactsService;
import com.hillel.artemjev.contactbook.services.contacts.ContactsService;
import com.hillel.artemjev.contactbook.services.user.ApiUserService;
import com.hillel.artemjev.contactbook.services.user.UserService;
import com.hillel.artemjev.contactbook.util.ContactDtoBuilder;
import com.hillel.artemjev.contactbook.util.UserDtoBuilder;
import com.hillel.artemjev.contactbook.util.httprequest.JsonHttpRequestFactory;
import lombok.AllArgsConstructor;

import java.net.http.HttpClient;

@AllArgsConstructor
public class ApiServiceFactory implements ServiceFactory {
    private final AppProperties appProperties;

    @Override
    public UserService createUserService() {
        ObjectMapper mapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return new ApiUserService(
                appProperties.getBaseUri(),
                new UserDtoBuilder(),
                new AccessToken(),
                mapper,
                new JsonHttpRequestFactory(mapper),
                HttpClient.newBuilder().build()
        );
    }

    @Override
    public ContactsService createContactsService(UserService userService) {
        ObjectMapper mapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return new ApiContactsService(
                userService,
                appProperties.getBaseUri(),
                new ContactDtoBuilder(),
                mapper,
                new JsonHttpRequestFactory(mapper),
                HttpClient.newBuilder().build()
        );
    }
}
