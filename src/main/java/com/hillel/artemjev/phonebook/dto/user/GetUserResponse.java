package com.hillel.artemjev.phonebook.dto.user;

import lombok.Data;

import java.util.List;

@Data
public class GetUserResponse {
    private String status;
    private List<User> users;
    private String error;

    @Data
    public static class User {
        private String login;
        private String password;
        private String dateBorn;
    }

    public boolean isSuccess() {
        return "ok".equalsIgnoreCase(status);
    }
}
