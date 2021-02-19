package com.hillel.artemjev.contactbook.util.contactparser;

import com.hillel.artemjev.contactbook.entities.Contact;

import java.util.List;

public interface ContactParser {

    String toString(Contact contact);

    Contact parse(String contactStr);

    public List<Contact> parseList(String contactsString, String separator);
}
