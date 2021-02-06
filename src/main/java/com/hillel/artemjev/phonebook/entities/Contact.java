package com.hillel.artemjev.phonebook.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    private Integer id;
    private String name;
    private ContactType type;
    private String contact;

    public enum ContactType {PHONE, EMAIL,}
}
