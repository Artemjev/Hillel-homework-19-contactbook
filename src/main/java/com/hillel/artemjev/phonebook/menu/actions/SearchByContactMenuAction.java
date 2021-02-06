package com.hillel.artemjev.phonebook.menu.actions;

import com.hillel.artemjev.phonebook.entities.Contact;
import com.hillel.artemjev.phonebook.menu.MenuAction;
import com.hillel.artemjev.phonebook.services.contacts.ContactsService;

import java.util.List;
import java.util.Scanner;

public class SearchByContactMenuAction implements MenuAction {
    private ContactsService contactsService;
    private Scanner sc;

    public SearchByContactMenuAction(ContactsService contactsService, Scanner sc) {
        this.contactsService = contactsService;
        this.sc = sc;
    }

    @Override
    public String getName() {
        return "Поиск по контакту";
    }

    @Override
    public void doAction() {
        if (!contactsService.isAuth()) {
            System.out.println("Время сеанса истекло. Неообходимо повторно войти в систему.\n");
            return;
        }
        System.out.println("\n*********************************");
        System.out.println("Поиск по контакту");
        System.out.print("Введите контакт или его часть: ");
        String contactToSearch = sc.nextLine();

        List<Contact> foundContactsList = contactsService.findByValue(contactToSearch);

        if (foundContactsList != null && foundContactsList.size() != 0) {
            System.out.println("Найдены следующие контакты:");
            foundContactsList.stream().forEach(System.out::println);
        } else {
            System.out.println("Контакты не найдены.");
        }
        System.out.println("*********************************");
    }

    @Override
    public boolean isVisible() {
        return contactsService.isAuth();
    }
}
