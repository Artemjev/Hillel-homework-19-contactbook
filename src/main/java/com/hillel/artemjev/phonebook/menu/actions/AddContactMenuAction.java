package com.hillel.artemjev.phonebook.menu.actions;

import com.hillel.artemjev.phonebook.entities.Contact;
import com.hillel.artemjev.phonebook.services.contacts.ContactsService;
import com.hillel.artemjev.phonebook.menu.MenuAction;

import java.util.Scanner;


public class AddContactMenuAction implements MenuAction {
    final private ContactsService contactsService;
    final private Scanner sc;

    public AddContactMenuAction(ContactsService contactsService, Scanner sc) {
        this.contactsService = contactsService;
        this.sc = sc;
    }

    @Override
    public void doAction() {
        if (!contactsService.isAuth()) {
            System.out.println("Время сеанса истекло. Неообходимо повторно войти в систему.\n");
            return;
        }
        System.out.println("\n*********************************");
        System.out.println("Добавление контакта");
        System.out.print("Введите имя: ");
        String name = sc.nextLine();

        System.out.print("Введите тип контакта (PHONE/EMAIL): ");
        Contact.ContactType type;
        try {
            type = Contact.ContactType.valueOf(sc.nextLine().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Некорректно введен тип контакта.");
            System.out.println("Контакт не добавлен.");
            System.out.println("*********************************");
            return;
        }

        System.out.printf("Введите %s: ", type);
        String contactStr = sc.nextLine();
        if (!validateContact(contactStr, type)) {
            System.out.printf("Некорректный формат ввода %s.\n", type);
            System.out.println("Контакт не добавлен.");
            System.out.println("*********************************");
            return;
        }
        Contact contact = new Contact();
        contact.setName(name);
        contact.setType(type);
        contact.setContact(contactStr);

        try {
            contactsService.add(contact);
        } catch (RuntimeException e) {
            System.out.println("Контакт не добавлен: " + e.getMessage());
            return;
        }


        System.out.println("Контакт добавлен");
        System.out.println("*********************************");
    }

    @Override
    public String getName() {
        return "Добавить контакт";
    }

    @Override
    public boolean isVisible() {
        return contactsService.isAuth();
    }

    //------------------------------------------------------------------------------
    private boolean validateContact(String contact, Contact.ContactType type) {
        boolean isValid = false;
        switch (type) {
            case PHONE:
                isValid = contact.matches("(?:\\+380|380|80|0)?(\\d{1,9})");
                break;
            case EMAIL:
                isValid = contact.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+");
                ;
                break;
        }
        return isValid;
    }
}


