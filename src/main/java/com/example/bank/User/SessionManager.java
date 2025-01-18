package com.example.bank.User;

public class SessionManager {
    private User currentUser;
    private static SessionManager instance;

    public SessionManager(){}

    public static SessionManager getInstance(){
        if(instance == null){
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(User user){
        currentUser = user;
    }

    public void logout(){
        currentUser = null;
    }

    public User getCurrentUser(){
        return currentUser;
    }

    public boolean isLoggedIn(){
        return currentUser != null;
    }
}
