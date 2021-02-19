package com.hillel.artemjev.contactbook;

import com.hillel.artemjev.contactbook.config.AppProperties;
import com.hillel.artemjev.contactbook.config.AppSystemProperties;
import com.hillel.artemjev.contactbook.config.ConfigLoader;
import com.hillel.artemjev.contactbook.menu.Menu;
import com.hillel.artemjev.contactbook.menu.actions.*;
import com.hillel.artemjev.contactbook.services.contacts.ContactsService;
import com.hillel.artemjev.contactbook.services.factory.ApiServiceFactory;
import com.hillel.artemjev.contactbook.services.factory.FileServiceFactory;
import com.hillel.artemjev.contactbook.services.factory.InMemoryServiceFactory;
import com.hillel.artemjev.contactbook.services.factory.ServiceFactory;
import com.hillel.artemjev.contactbook.services.user.UserService;

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
        AppProperties appProperties = configLoader.getFileProps(AppProperties.class, configFileName);
        appProperties.checkPropertiesExists();

        ServiceFactory serviceFactory;

        switch (appProperties.getMode()) {
            case ("api"):
                serviceFactory = new ApiServiceFactory(appProperties);
                break;
            case ("file"):
                serviceFactory = new FileServiceFactory(appProperties);
                break;
            case ("memory"):
                serviceFactory = new InMemoryServiceFactory();
                break;
            default:
                System.out.printf("The app.service.workmode parameter was not set in the %s file, " +
                        "or it was set incorrectly.", configFileName);
                return;
        }

        UserService userService = serviceFactory.createUserService();
        ContactsService contactsService = serviceFactory.createContactsService(userService);

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
