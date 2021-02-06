package com.hillel.artemjev.phonebook.dto.user;

import lombok.Data;

@Data
public class LoginResponse {
    private String status;
    private String error;
    private String token;

    public boolean isSuccess() {
        return "ok".equalsIgnoreCase(status);
    }
}
