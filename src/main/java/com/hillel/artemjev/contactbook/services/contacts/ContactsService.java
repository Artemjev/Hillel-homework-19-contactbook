package com.hillel.artemjev.contactbook.services.contacts;


import com.hillel.artemjev.contactbook.entities.Contact;

import java.util.List;

public interface ContactsService {

    void remove(Integer id);

    void add(Contact contact);

    List<Contact> getAll();

    List<Contact> findByName(String name);

    List<Contact> findByValue(String value);

    boolean isAuth();
}
