package com.hillel.artemjev.phonebook.dto.contacts;

import com.hillel.artemjev.phonebook.entities.Contact;
import lombok.Data;

import java.util.List;

@Data
public class ContactsResponse {
    private String status;
    private List<Contact> contacts;
    private String error;
}
