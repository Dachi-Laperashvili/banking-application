package com.example.bank.Account;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AccountRepository {

    private String dbUrl = "jdbc:mysql://127.0.0.1:3306/bank";
    private String dbUser = "root";
    private String dbPassword = System.getenv("DB_PASSWORD");
    private ArrayList<Account> accounts = new ArrayList<>();

    public void add(Account account){
        try(Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO accounts (fullName,password,personal_id) VALUES (?, ?, ?)")){

            statement.setString(1, account.getFullName());
            statement.setString(2,account.getPassword());
            statement.setLong(3,account.getPersonalId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Account findAccountByPersonalId(long personal_id){
        try(Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts WHERE personal_id = ?")){

            statement.setLong(1,personal_id);

            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    Account acc = new Account(resultSet.getString("fullName"),resultSet.getLong("personal_id"),resultSet.getString("password"));
                    return acc;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
