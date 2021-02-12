package com.hillel.artemjev.contactbook.entities;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private String login;
    private String password;
    private LocalDate dateBorn;
}
