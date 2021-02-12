package com.hillel.artemjev.contactbook.dto;

import lombok.Data;

@Data
public class StatusResponse {
    private String status;
    private String error;

    public boolean isSuccess() {
        return "ok".equalsIgnoreCase(status);
    }
}
