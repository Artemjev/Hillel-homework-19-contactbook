package com.hillel.artemjev.phonebook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.hillel.artemjev.phonebook.entities.Contact;
import com.hillel.artemjev.phonebook.menu.Menu;
import com.hillel.artemjev.phonebook.menu.actions.*;
import com.hillel.artemjev.phonebook.services.AccessToken;
import com.hillel.artemjev.phonebook.services.contacts.ApiContactsService;
import com.hillel.artemjev.phonebook.services.contacts.ContactsService;
import com.hillel.artemjev.phonebook.services.contacts.FileContactsService;
import com.hillel.artemjev.phonebook.services.contacts.InMemoryContactsService;
import com.hillel.artemjev.phonebook.services.user.ApiUserService;
import com.hillel.artemjev.phonebook.services.user.FictiveUserService;
import com.hillel.artemjev.phonebook.services.user.UserService;
import com.hillel.artemjev.phonebook.util.ContactDtoBuilder;
import com.hillel.artemjev.phonebook.util.DefaultContactParser;
import com.hillel.artemjev.phonebook.util.UserDtoBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.http.HttpClient;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        String propertiesProfile = System.getProperties().getProperty("contactbook.profile");

        String propertiesFile = "";
        switch (propertiesProfile) {
            case ("dev"):
                propertiesFile = "app-dev.properties";
                break;
            case ("prod"):
                propertiesFile = "app-prod.properties";
                break;
            default:
                System.out.println("Contactbook.profile launch parameter not set");
                return;
        }

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(propertiesFile));
        } catch (IOException e) {
            System.out.println("Properties file loading problem: " + e.getMessage());
            e.printStackTrace();
        }

        String workMode = properties.getProperty("app.service.workmode");

        UserService userService = null;
        ContactsService contactsService = null;

        switch (workMode) {
            case ("api"):
                String baseUri = (String) properties.get("api.base-url");
                UserDtoBuilder userDtoBuilder = new UserDtoBuilder();
                AccessToken token = new AccessToken();
                ObjectMapper mapper = new ObjectMapper()
                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
                HttpClient httpClient = HttpClient.newBuilder().build();
                userService = new ApiUserService(
                        baseUri,
                        userDtoBuilder,
                        token,
                        mapper,
                        httpClient
                );
                ContactDtoBuilder contactDtoBuilder = new ContactDtoBuilder();
                contactsService = new ApiContactsService(
                        userService,
                        baseUri,
                        contactDtoBuilder,
                        mapper,
                        httpClient
                );
                break;

            case ("file"):
                String filePath = (String) properties.get("file.path");
                userService = new FictiveUserService();
                contactsService = new FileContactsService(
                        userService,
                        new DefaultContactParser(),
                        "filePath");

                break;

            case ("memory"):
                userService = new FictiveUserService();
                contactsService = new InMemoryContactsService(userService, new LinkedList<Contact>());
                break;
            default:
                System.out.printf("The app.service.workmode parameter was not set in the %s file, " +
                        "or it was set incorrectly.", propertiesFile);
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
