package com.hillel.artemjev.contactbook.config;

import com.hillel.artemjev.contactbook.annotations.PropertyName;
import lombok.Data;

@Data
public class AppSystemProperties {
    @PropertyName("contactbook.profile")
    private String profile;
}