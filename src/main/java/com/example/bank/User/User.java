package com.example.bank.User;

public class User {
    private String firstName;
    private String fullName;
    private String lastName;
    private long personal_id;
    private String password;

    public User(String firstName, String lastName, long personal_id, String password){
        this.personal_id = personal_id;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        fullName = firstName + " " + lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName){this.firstName = firstName;}

    public String getLastName(){ return lastName;}

    public void setLastName(String lastName){ this.lastName = lastName;}

    public long getPersonalId(){return personal_id;}

    public void setPersonal_id(long personal_id) {this.personal_id = personal_id;}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
