package com.example.bank;

public class Account {
    private String fullName;
    private String id;
    private String password;

    public Account(String firstName,String lastName, String id, String password){
        this.id = id;
        this.password = password;
        fullName = firstName + " " + lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
