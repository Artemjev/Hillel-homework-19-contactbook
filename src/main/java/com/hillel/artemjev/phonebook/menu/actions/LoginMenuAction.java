package com.hillel.artemjev.phonebook.menu.actions;

import com.hillel.artemjev.phonebook.entities.User;
import com.hillel.artemjev.phonebook.menu.MenuAction;
import com.hillel.artemjev.phonebook.services.user.UserService;
import lombok.AllArgsConstructor;

import java.util.Scanner;

@AllArgsConstructor
public class LoginMenuAction implements MenuAction {
    final private UserService userService;
    final private Scanner sc;

    @Override
    public String getName() {
        return "Войти";
    }

    @Override
    public void doAction() {
        System.out.print("введите логин: ");
        String login = sc.nextLine();

        System.out.print("введите пароль: ");
        String password = sc.nextLine();

        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        try {
            userService.login(user);
        } catch (RuntimeException e) {
            System.out.println("Логин не состоялся: " + e.getMessage());
            return;
        }
    }

    @Override
    public boolean isVisible() {
        return !userService.isAuth();
    }

    @Override
    public boolean closeAfter() {
        return false;
    }
}
