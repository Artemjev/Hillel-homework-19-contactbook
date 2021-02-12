package com.hillel.artemjev.contactbook.services.user;

import com.hillel.artemjev.contactbook.entities.User;

import java.util.List;

public interface UserService {

    String getToken();

    boolean isAuth();

    void register(User user);

    void login(User user);

    List<User> getAll();

}
