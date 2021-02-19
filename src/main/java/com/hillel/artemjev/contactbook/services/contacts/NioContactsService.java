package com.hillel.artemjev.contactbook.services.contacts;

import com.hillel.artemjev.contactbook.entities.Contact;
import com.hillel.artemjev.contactbook.services.user.UserService;
import com.hillel.artemjev.contactbook.util.contactparser.DefaultContactParser;
import com.hillel.artemjev.contactbook.util.NioFileUtil;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class NioContactsService implements ContactsService {
    private final UserService userService;
    private final Path path;
    private final DefaultContactParser parser;
    private final NioFileUtil fileUtil;

    public void remove(Integer id) {
        List<Contact> contactList = getAll().stream()
                .filter(c -> !c.getId().equals(id))
                .collect(Collectors.toList());
        fileUtil.cleanFile();
        for (Contact contact : contactList) {
            fileUtil.writeString(parser.toString(contact) + "\n");
        }
    }

    @Override
    public void add(Contact contact) {
        String contactStr = parser.toString(contact) + "\n";
        if (contactStr != null) {
            fileUtil.writeString(contactStr);
        }
    }

    @Override
    public List<Contact> getAll() {
        return readContacts(contact -> true);
    }

    @Override
    public List<Contact> findByValue(String value) {
        return readContacts(contact -> contact.getContact().contains(value));
    }

    @Override
    public List<Contact> findByName(String name) {
        return readContacts(contact -> contact.getName().toUpperCase().startsWith(name.toUpperCase()));
    }

    @Override
    public boolean isAuth() {
        return this.userService.isAuth();
    }

    //------------------------------------------------------------------
    private int newId() {
        return getAll().stream()
                .map(contact -> contact.getId())
                .max(Comparator.comparingInt(a -> a))
                .map(id -> id + 1)
                .orElse(1);
    }

    private List<Contact> readContacts(Predicate<Contact> predicate) {
        List<Contact> contactList = new LinkedList<>();
        fileUtil.readByLine(str -> {
            Contact contact = parser.parse(str);
            if (predicate.test(contact)) {
                contactList.add(contact);
            }
        });
        return contactList;
    }
}
