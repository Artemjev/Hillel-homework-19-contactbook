package com.hillel.artemjev.phonebook.util;

import com.hillel.artemjev.phonebook.dto.contacts.*;
import com.hillel.artemjev.phonebook.entities.Contact;

public class ContactDtoBuilder {

    public AddContactRequest getAddContactRequest(Contact contact) {
        AddContactRequest request = new AddContactRequest();
        request.setName(contact.getName());
        request.setType(contact.getType().toString().toLowerCase());
        request.setValue(contact.getContact());
        return request;
    }

    public FindByNameContactRequest getFindByNameContactRequest(String name) {
        FindByNameContactRequest request = new FindByNameContactRequest();
        request.setName(name);
        return request;
    }

    public FindByValueContactRequest getFindByValueContactRequest(String value) {
        FindByValueContactRequest request = new FindByValueContactRequest();
        request.setValue(value);
        return request;
    }

    public Contact getContact(GetContactsResponse.Contact contactResponse) {
        Contact contact = new Contact();
        contact.setId(contactResponse.getId());
        contact.setName(contactResponse.getName());
        contact.setType(Contact.ContactType.valueOf(contactResponse.getType().toUpperCase()));
        contact.setContact(contactResponse.getValue());
        return contact;
    }

    public Contact getContact(PostContactsResponse.Contact contactResponse) {
        Contact contact = new Contact();
        contact.setId(contactResponse.getId());
        contact.setName(contactResponse.getName());
        contact.setType(Contact.ContactType.valueOf(contactResponse.getType().toUpperCase()));
        contact.setContact(contactResponse.getValue());
        return contact;
    }
}
