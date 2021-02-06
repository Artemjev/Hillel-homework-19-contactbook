package com.hillel.artemjev.phonebook.services.user;

import com.hillel.artemjev.phonebook.entities.User;

import java.util.List;

public interface UserService {

    String getToken();

    boolean isAuth();

    void register(User user);

    void login(User user);

    List<User> getAll();

}
