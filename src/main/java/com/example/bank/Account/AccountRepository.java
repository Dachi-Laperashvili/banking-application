package com.example.bank.Account;

import com.example.bank.User.User;
import com.example.bank.User.UserRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AccountRepository {
    private String dbUrl = "jdbc:mysql://127.0.0.1:3306/bank";
    private String dbUser = "root";
    private String dbPassword = System.getenv("DB_PASSWORD");

    public void add(BankAccount account){
        try(Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO accounts (account_id,balance,type,user_id) VALUES (?, ?, ?, ?)")){

            statement.setLong(1,account.getAccountId());
            statement.setBigDecimal(2,account.getBalance());
            statement.setString(3,account.getAccountType().toString());
            statement.setLong(4,account.getUser().getId());

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<BankAccount> findAccountByUserId(Long user_id){
        List<BankAccount> accounts = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts WHERE user_id = ?")){
            statement.setLong(1,user_id);

            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    User user = new UserRepository().findUserByPersonalId(user_id);
                    BankAccount bankAccount = new BankAccount(resultSet.getLong("account_id"),resultSet.getBigDecimal("balance"),user,AccountType.valueOf(resultSet.getString("type")));

                    accounts.add(bankAccount);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accounts;
    }
}
