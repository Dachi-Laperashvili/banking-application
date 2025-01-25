package com.example.bank.Account;

import com.example.bank.User.User;

import java.math.BigDecimal;
import java.security.SecureRandom;

public class BankAccount {
    private Long accountId;
    private BigDecimal balance;
    private User user;
    private AccountType accountType;

    public BankAccount(BigDecimal balance, User user, AccountType accountType){
        this.accountId = generateId();
        this.balance = balance;
        this.user = user;
        this.accountType = accountType;
    }

    public BankAccount(Long accountId,BigDecimal balance, User user, AccountType accountType){
        this.accountId = accountId;
        this.balance = balance;
        this.user = user;
        this.accountType = accountType;
    }

    private Long generateId(){
        SecureRandom random = new SecureRandom();
        return 1_000_000_000_000_000L + (long) (random.nextDouble() * 9_000_000_000_000_000L);
    }
    public Long getAccountId(){
        return accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public User getUser() {
        return user;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
