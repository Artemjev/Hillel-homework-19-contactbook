package com.hillel.artemjev.phonebook.menu.actions;

import com.hillel.artemjev.phonebook.entities.Contact;
import com.hillel.artemjev.phonebook.services.contacts.ContactsService;
import com.hillel.artemjev.phonebook.menu.MenuAction;

import java.util.List;

public class ReadAllContactsMenuAction implements MenuAction {

    private ContactsService contactsService;

    public ReadAllContactsMenuAction(ContactsService contactsService) {
        this.contactsService = contactsService;
    }

    @Override
    public void doAction() {
        if (!contactsService.isAuth()) {
            System.out.println("Время сеанса истекло. Неообходимо повторно войти в систему.\n");
            return;
        }
        System.out.println("\n*********************************");
        System.out.println("Список контактов:");
        List<Contact> contacts = contactsService.getAll();
        int i = 0;
        if (contacts != null && contacts.size() != 0) {
            for (Contact contact : contacts) {
                System.out.printf("%3d -  %s, %s: %s\n",
                        contact.getId(),
                        contact.getName(),
                        contact.getType(),
                        contact.getContact());
            }
        } else {
            System.out.println("Список пуст.");
        }
        System.out.println("*********************************");
    }

    @Override
    public String getName() {
        return "Посмотреть контакты";
    }

    @Override
    public boolean isVisible() {
        return contactsService.isAuth();
    }
}
