package com.hillel.artemjev.contactbook.services.contacts;

import com.hillel.artemjev.contactbook.entities.Contact;
import com.hillel.artemjev.contactbook.services.user.UserService;
import com.hillel.artemjev.contactbook.util.contactparser.ContactParser;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class FileContactsService implements ContactsService {
    private final UserService userService;
    private final ContactParser parser;
    private final String filePath;

    @Override
    public void add(Contact contact) {
        contact.setId(newId());
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(filePath, true), true)) {
            writer.println(parser.toString(contact));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //из файла в файл
    @Override
    public void remove(Integer id) {
        List<Contact> contactList = getAll().stream()
                .filter(c -> !c.getId().equals(id))
                .collect(Collectors.toList());
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(filePath, true), true)) {
            Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.TRUNCATE_EXISTING);
            contactList.stream().forEach(contact -> writer.println(parser.toString(contact)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));) {
            String str;
            while ((str = br.readLine()) != null) {
                Contact contact = parser.parse(str);
                if (predicate.test(contact)) {
                    contactList.add(contact);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contactList;
    }
}
