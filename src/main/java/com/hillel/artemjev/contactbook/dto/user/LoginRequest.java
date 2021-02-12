package com.hillel.artemjev.contactbook.dto.user;

import lombok.Data;

@Data
public class LoginRequest {
    private String login;
    private String password;
}
