package com.hillel.artemjev.contactbook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.hillel.artemjev.contactbook.config.AppProperties;
import com.hillel.artemjev.contactbook.config.AppSystemProperties;
import com.hillel.artemjev.contactbook.config.ConfigLoader;
import com.hillel.artemjev.contactbook.entities.Contact;
import com.hillel.artemjev.contactbook.menu.Menu;
import com.hillel.artemjev.contactbook.menu.actions.*;
import com.hillel.artemjev.contactbook.services.AccessToken;
import com.hillel.artemjev.contactbook.services.contacts.ApiContactsService;
import com.hillel.artemjev.contactbook.services.contacts.ContactsService;
import com.hillel.artemjev.contactbook.services.contacts.FileContactsService;
import com.hillel.artemjev.contactbook.services.contacts.InMemoryContactsService;
import com.hillel.artemjev.contactbook.services.user.ApiUserService;
import com.hillel.artemjev.contactbook.services.user.FictiveUserService;
import com.hillel.artemjev.contactbook.services.user.UserService;
import com.hillel.artemjev.contactbook.util.ContactDtoBuilder;
import com.hillel.artemjev.contactbook.util.DefaultContactParser;
import com.hillel.artemjev.contactbook.util.UserDtoBuilder;

import java.net.http.HttpClient;
import java.util.LinkedList;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        ConfigLoader configLoader = new ConfigLoader();
        AppSystemProperties systemProperties = configLoader.getSystemProps(AppSystemProperties.class);

        String configFileName;
        if (systemProperties.getProfile() != null) {
            configFileName = "app-" + systemProperties.getProfile() + ".properties";
        } else {
            System.out.println("Contactbook.profile launch parameter not set");
            return;
        }
        AppProperties config = configLoader.getFileProps(AppProperties.class, configFileName);
        config.checkPropertiesExists();

        UserService userService = null;
        ContactsService contactsService = null;

        switch (config.getMode()) {
            case ("api"):
                UserDtoBuilder userDtoBuilder = new UserDtoBuilder();
                AccessToken token = new AccessToken();
                ObjectMapper mapper = new ObjectMapper()
                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
                HttpClient httpClient = HttpClient.newBuilder().build();
                userService = new ApiUserService(
                        config.getBaseUri(),
                        userDtoBuilder,
                        token,
                        mapper,
                        httpClient
                );
                ContactDtoBuilder contactDtoBuilder = new ContactDtoBuilder();
                contactsService = new ApiContactsService(
                        userService,
                        config.getBaseUri(),
                        contactDtoBuilder,
                        mapper,
                        httpClient
                );
                break;

            case ("file"):
                userService = new FictiveUserService();
                contactsService = new FileContactsService(
                        userService,
                        new DefaultContactParser(),
                        config.getFilePath());
                break;

            case ("memory"):
                userService = new FictiveUserService();
                contactsService = new InMemoryContactsService(userService, new LinkedList<Contact>());
                break;

            default:
                System.out.printf("The app.service.workmode parameter was not set in the %s file, " +
                        "or it was set incorrectly.", configFileName);
                return;
        }

        Scanner sc = new Scanner(System.in);
        Menu menu = new Menu(sc);
        menu.addAction(new LoginMenuAction(userService, sc));
        menu.addAction(new RegistrationMenuAction(userService, sc));
        menu.addAction(new ReadAllUsersMenuAction(userService, sc));
        menu.addAction(new ReadAllContactsMenuAction(contactsService));
        menu.addAction(new SearchByNameMenuAction(contactsService, sc));
        menu.addAction(new SearchByContactMenuAction(contactsService, sc));
        menu.addAction(new AddContactMenuAction(contactsService, sc));
        menu.addAction(new ExitMenuAction());
        menu.run();
    }
}
