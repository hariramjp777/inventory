package com.inventory.model.objects.user;

public class User {
    private String firstName;
    private String lastName;
    private String emailID;
    private String password;
    private boolean isVerified;

    public User(String firstName, String lastName, String emailID, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailID = emailID;
        this.password = password;
        this.isVerified = false;
    }

    public User(String firstName, String lastName, String emailID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailID = emailID;
        this.isVerified = false;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmailID() {
        return this.emailID;
    }

    public String getName() {
        return this.firstName + " " + this.lastName;
    }

    public boolean isVerified() {
        return this.isVerified;
    }

    @Override
    public String toString() {
        return "User {" + this.firstName + " " + this.lastName + "} ";
    }
}
