package com.cwu.library;

class Person {
    private int id;
    private String firstName;
    private String lastName;

    /**
     * Constructor for Person class.
     * @param id
     * @param firstName
     * @param lastName
     */
    public Person(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String toString() {
        return "Id:" + id + " Name:" + firstName + " " + lastName;
    }
}