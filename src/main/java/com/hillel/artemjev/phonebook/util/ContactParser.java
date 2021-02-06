package com.hillel.artemjev.phonebook.util;

import com.hillel.artemjev.phonebook.entities.Contact;

import java.util.List;

public interface ContactParser {

    String toString(Contact contact);

    Contact parse(String contactStr);

    public List<Contact> parseList(String contactsString, String separator);
}
