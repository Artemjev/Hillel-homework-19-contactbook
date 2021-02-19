package com.hillel.artemjev.contactbook.services.factory;

import com.hillel.artemjev.contactbook.services.contacts.ContactsService;
import com.hillel.artemjev.contactbook.services.user.UserService;

public interface ServiceFactory {
    UserService createUserService();

    ContactsService createContactsService(UserService userService);
}
