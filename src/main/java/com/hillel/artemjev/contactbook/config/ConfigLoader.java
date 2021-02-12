package com.hillel.artemjev.contactbook.config;

import com.hillel.artemjev.contactbook.annotations.PropertyName;
import com.hillel.artemjev.contactbook.exception.FailLoadPropertiesFromFileException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class ConfigLoader {

    public <T> T getSystemProps(Class<T> clazz) {
        Object object = createObject(clazz);
        extractProps(object, System.getProperties());
        return (T) object;
    }

    public <T> T getFileProps(Class<T> clazz, String file) {
        try (InputStream is = new FileInputStream(file)) {
            Properties properties = new Properties();
            properties.load(is);
            Object object = createObject(clazz);
            extractProps(object, properties);
            return (T) object;
        } catch (IOException e) {
            throw new FailLoadPropertiesFromFileException("FAIL LOAD PROPERTIES FROM " + file, e);
        }
    }

    //------------------------------------------------------------------------------------
    private void extractProps(Object object, Properties properties) {
        Class clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(PropertyName.class)) continue;
            PropertyName annotation = field.getAnnotation(PropertyName.class);
            String propertyName = annotation.value();
            String value = properties.getProperty(propertyName);
            field.setAccessible(true);
            try {
                field.set(object, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private Object createObject(Class clazz) {
        try {
            Constructor constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Need default constructor", e);
        }
    }
}
