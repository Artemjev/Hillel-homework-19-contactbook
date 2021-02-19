package com.hillel.artemjev.contactbook.services.factory;

import com.hillel.artemjev.contactbook.config.AppProperties;
import com.hillel.artemjev.contactbook.services.contacts.ContactsService;
import com.hillel.artemjev.contactbook.services.contacts.FileContactsService;
import com.hillel.artemjev.contactbook.services.user.FictiveUserService;
import com.hillel.artemjev.contactbook.services.user.UserService;
import com.hillel.artemjev.contactbook.util.contactparser.DefaultContactParser;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FileServiceFactory implements ServiceFactory {
    private final AppProperties appProperties;

    @Override
    public UserService createUserService() {
        return new FictiveUserService();
    }

    @Override
    public ContactsService createContactsService(UserService userService) {
        return new FileContactsService(
                userService,
                new DefaultContactParser(),
                appProperties.getFilePath());
    }
}
