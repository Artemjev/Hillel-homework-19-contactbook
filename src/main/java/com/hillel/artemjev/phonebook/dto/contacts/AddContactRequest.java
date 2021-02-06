package com.hillel.artemjev.phonebook.dto.contacts;

import lombok.Data;

@Data
public class AddContactRequest {
    private String name;
    private String type;
    private String value;
}
