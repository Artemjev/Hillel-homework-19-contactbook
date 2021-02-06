package com.hillel.artemjev.phonebook.util;

import com.hillel.artemjev.phonebook.entities.Contact;
import com.hillel.artemjev.phonebook.exception.ContactParseException;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultContactParser implements ContactParser {
    private static final String CONTACT_REGEX_PATTERN = "(\\d+)\\[(\\w+):(.+)\\](.+)";
    private Pattern pattern;

    public DefaultContactParser() {
        this.pattern = Pattern.compile(CONTACT_REGEX_PATTERN);
    }

    public Contact parse(String contactStr) {
        Matcher matcher = pattern.matcher(contactStr);
        if (matcher.matches()) {
            String id = matcher.group(1);
            String type = matcher.group(2);
            String cont = matcher.group(3);
            String name = matcher.group(4);
            Contact contact = new Contact();
            contact.setId(Integer.parseInt(id));
            contact.setName(name);
            contact.setType(Contact.ContactType.valueOf(type.toUpperCase()));
            contact.setContact(cont);
            return contact;
        } else
            throw new ContactParseException("invalid string for parsing");
    }

    public String toString(Contact contact) {
        return String.format(
                "%d[%s:%s]%s",
                contact.getId(),
                contact.getType().toString().toLowerCase(),
                contact.getContact(),
                contact.getName()
        );
    }

    public List<Contact> parseList(String contactsString, String separator) {
        List<Contact> contactList = new LinkedList<>();
        List<String> contactStrList = getContactStringsList(contactsString, separator);
        for (String s : contactStrList) {
            contactList.add(parse(s));
        }
        return contactList;
    }

    //--------------------------------------------------------------------------------
    private List<String> getContactStringsList(String contactsStr, String separator) {
        String[] contactStringsArray = contactsStr.split(separator);
        List<String> contactStringsList = new LinkedList<>();
        for (String str : contactStringsArray) {
            str = str.trim();
            if (str.length() > 0) {
                contactStringsList.add(str);
            }
        }
        return contactStringsList;
    }
}
