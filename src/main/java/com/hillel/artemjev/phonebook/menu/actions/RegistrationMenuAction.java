package com.hillel.artemjev.phonebook.menu.actions;

import com.hillel.artemjev.phonebook.entities.User;
import com.hillel.artemjev.phonebook.menu.MenuAction;
import com.hillel.artemjev.phonebook.services.user.UserService;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

@AllArgsConstructor
public class RegistrationMenuAction implements MenuAction {
    final private UserService userService;
    final private Scanner sc;

    @Override
    public void doAction() {
        System.out.print("введите логин: ");
        String login = sc.nextLine();

        System.out.print("введите пароль: ");
        String password = sc.nextLine();

        System.out.print("введите дату рождения: ");
        String dateBorn = sc.nextLine();

        LocalDate date = LocalDate.parse(dateBorn, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setDateBorn(date);
        try {
            userService.register(user);
        } catch (RuntimeException e) {
            System.out.println("Регистрация не осуществленна: " + e.getMessage());
            return;
        }
    }

    @Override
    public String getName() {
        return "Зарегистрироваться";
    }

    @Override
    public boolean isVisible() {
        return !userService.isAuth();
    }
}
