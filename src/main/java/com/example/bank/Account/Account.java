package com.example.bank.Account;

public class Account {
    private String fullName;
    private long personal_id;
    private String password;

    public Account(String firstName,String lastName,long personal_id, String password){
        this.personal_id = personal_id;
        this.password = password;
        fullName = firstName + " " + lastName;
    }

    public Account(String fullName,long personal_id, String password){
        this.personal_id = personal_id;
        this.password = password;
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public long getPersonalId(){return personal_id;}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
