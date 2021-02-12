package com.hillel.artemjev.contactbook.dto.contacts;

import com.hillel.artemjev.contactbook.entities.Contact;
import lombok.Data;

import java.util.List;

@Data
public class ContactsResponse {
    private String status;
    private List<Contact> contacts;
    private String error;
}
