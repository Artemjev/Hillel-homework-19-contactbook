package com.hillel.artemjev.phonebook.menu;

public interface MenuAction {

    String getName();

    void doAction();

    default boolean closeAfter() {
        return false;
    }

    default boolean isVisible() {
        return true;
    }
}
