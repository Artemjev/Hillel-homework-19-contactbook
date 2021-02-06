package com.hillel.artemjev.phonebook.menu.actions;

import com.hillel.artemjev.phonebook.services.contacts.ContactsService;
import com.hillel.artemjev.phonebook.menu.MenuAction;

import java.util.Scanner;


public class RemoveContactMenuAction implements MenuAction {
    private ContactsService contactsService;
    private Scanner sc;

    public RemoveContactMenuAction(ContactsService contactsService, Scanner sc) {
        this.contactsService = contactsService;
        this.sc = sc;
    }

    @Override
    public void doAction() {
        System.out.println("\n*********************************");
        System.out.println("Удаление контакта");
        System.out.print("Введите порядковый номер контакта для удаления: ");

        if (!sc.hasNextInt()) {
            System.out.println("Ошибка удаления. Порядковый номер должен быть целым числом.");
            System.out.println("*********************************");
            sc.nextLine();
            return;
        }

        int index = sc.nextInt();

        if (index > contactsService.getAll().size() || index <= 0) {
            System.out.printf("В справочнике всего %d контактов (отсчет с 1).\n", contactsService.getAll().size());
            System.out.println("*********************************");
            return;
        }

        --index;
        String strRemovedContact = contactsService.getAll().get(index).toString();
        contactsService.remove(index);
        System.out.println("Контакт [" + strRemovedContact + "] удален");
        System.out.println("*********************************");
    }

    @Override
    public String getName() {
        return "Удалить контакт";
    }

    @Override
    public boolean isVisible() {
        return contactsService.isAuth();
    }
}
