package com.hillel.artemjev.phonebook.services.user;

import com.hillel.artemjev.phonebook.entities.User;

import java.util.List;

public class FictiveUserService implements UserService {
    @Override
    public String getToken() {
        return null;
    }

    @Override
    public boolean isAuth() {
        return true;
    }

    @Override
    public void register(User user) {
        throw new UnsupportedOperationException("method \"register\" not supported");
    }

    @Override
    public void login(User user) {
        throw new UnsupportedOperationException("method \"login\" not supported");
    }

    @Override
    public List<User> getAll() {
        return null;
    }

}
