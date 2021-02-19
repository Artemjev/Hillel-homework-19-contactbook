package com.hillel.artemjev.contactbook.config;

import com.hillel.artemjev.contactbook.annotations.PropertyName;
import com.hillel.artemjev.contactbook.exception.FailLoadPropertiesFromFileException;
import lombok.Data;

@Data
public class AppProperties {
    @PropertyName("app.service.workmode")
    private String mode;

    @PropertyName("api.base-uri")
    private String baseUri;

    @PropertyName("file.path")
    private String filePath;

    public void checkPropertiesExists() {
        if (this.mode == null) {
            throw new FailLoadPropertiesFromFileException("\"mode\" property is empty");
        }
        if (this.mode.equals("api") && baseUri == null) {
            throw new FailLoadPropertiesFromFileException("\"baseUri\" property is empty for \"api\" mode");
        }
        if (this.mode.equals("file") && filePath == null) {
            throw new FailLoadPropertiesFromFileException("\"filePath\" property is empty for \"file\" mode");
        }
    }
}
