package com.hillel.artemjev.phonebook.dto.contacts;

import lombok.Data;

import java.util.List;

@Data
public class GetContactsResponse {
    private String status;
    private String error;
    private List<Contact> contacts;

    @Data
    public static class Contact {
        private Integer id;
        private String name;
        private String value;
        private String type;
    }

    public boolean isSuccess() {
        return "ok".equalsIgnoreCase(status);
    }
}
