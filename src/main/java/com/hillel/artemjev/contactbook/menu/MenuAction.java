package com.hillel.artemjev.contactbook.menu;

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
