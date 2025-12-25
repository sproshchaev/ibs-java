package ru.ibs.example.person;

public class PersonBuilder {

    public static void main(String[] args) {
        int id = 23;
        String name = "John";
        Person person = null;
        person = buildPerson(id, name);
    }

    private static Person buildPerson(int id, String name) {
        return new Person(id, name);
    }
}