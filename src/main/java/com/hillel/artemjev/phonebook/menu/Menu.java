package com.hillel.artemjev.phonebook.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class Menu {
    private List<MenuAction> actions;
    private Scanner sc;

    public Menu(Scanner scanner) {
        this.actions = new ArrayList<>();
        this.sc = scanner;
    }

    public Menu(List<MenuAction> actions, Scanner sc) {
        this.actions = actions;
        this.sc = sc;
    }

    public void addAction(MenuAction action) {
        actions.add(action);
    }

    public void run() {
        while (true) {
            System.out.println("\nВыберете пункт меню:");
            List<MenuAction> menu = actions.stream()
                    .filter(action -> action.isVisible())
                    .collect(Collectors.toList());

            for (int i = 0; i < menu.size(); i++) {
                System.out.printf("%2d - %s\n", i + 1, menu.get(i).getName());
            }
            int choice = getMenuIndexFromUser();
            --choice;
            if (choice < 0 && choice >= menu.size()) {
                System.out.printf("Значение должно быть от 1 до %d.\n", menu.size());
                continue;
            }
            menu.get(choice).doAction();
            if (menu.get(choice).closeAfter()) break;
        }
    }

    //-----------------------------------------------------------------------------------------------
    private int getMenuIndexFromUser() {
        while (true) {
            System.out.print("\nEnter your choice: ");
            if (!sc.hasNextInt()) {
                System.out.println("Значение должно быть целым числом.");
                sc.nextLine();
            } else
                break;
        }
        int choice = sc.nextInt();
        sc.nextLine();
        return choice;
    }
}