package com.hillel.artemjev.phonebook.dto.user;

import lombok.Data;

@Data
public class LoginRequest {
    private String login;
    private String password;
}
