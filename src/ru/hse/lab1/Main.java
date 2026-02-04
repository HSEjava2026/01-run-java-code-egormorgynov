package ru.hse.lab1;

import ru.hse.lab1.second.Person;

/**
 * Main entry point.
 * @author Егор Моргунов
 * @version 1.0
 * @since 2026
 */
public class Main {
    public static void main(String[] args) {
        Person p = new Person("Иван", 20);
        System.out.println("Имя: " + p.getName());
        System.out.println("Возраст: " + p.getAge());
    }
}
