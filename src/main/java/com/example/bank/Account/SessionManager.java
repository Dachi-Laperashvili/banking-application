package com.example.bank.Account;

public class SessionManager {
    private Account currentAcc;
    private static SessionManager instance;

    public SessionManager(){}

    public static SessionManager getInstance(){
        if(instance == null){
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(Account account){
        currentAcc = account;
    }

    public void logout(){
        currentAcc = null;
    }

    public Account getCurrentAcc(){
        return currentAcc;
    }

    public boolean isLoggedIn(){
        return currentAcc != null;
    }
}
