package ru.hse.lab1.second;

/**
 * Represents a person with name and age.
 * @author Егор Моргунов
 * @version 1.0
 * @since 2026
 */


public class Person {
    private final String name;
    private final int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
