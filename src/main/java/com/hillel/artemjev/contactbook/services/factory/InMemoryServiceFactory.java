package com.hillel.artemjev.contactbook.services.factory;

import com.hillel.artemjev.contactbook.entities.Contact;
import com.hillel.artemjev.contactbook.services.contacts.ContactsService;
import com.hillel.artemjev.contactbook.services.contacts.InMemoryContactsService;
import com.hillel.artemjev.contactbook.services.user.FictiveUserService;
import com.hillel.artemjev.contactbook.services.user.UserService;

import java.util.LinkedList;

public class InMemoryServiceFactory implements ServiceFactory {

    @Override
    public UserService createUserService() {
        return new FictiveUserService();
    }

    @Override
    public ContactsService createContactsService(UserService userService) {
        return new InMemoryContactsService(
                userService,
                new LinkedList<Contact>()
        );
    }
}
