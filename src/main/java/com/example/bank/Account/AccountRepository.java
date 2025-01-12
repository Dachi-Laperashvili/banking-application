package com.example.bank.Account;

import java.util.ArrayList;

public class AccountRepository {
    private ArrayList<Account> accounts = new ArrayList<>();

    public ArrayList<Account> getAccounts(){
        return accounts;
    }

    public void add(Account account){
        accounts.add(account);
    }
    public Account findAccountById(long id){
        for(Account account: accounts){
            if(account.getId() == id){
                return account;
            }
        }
        return null;
    }
}
