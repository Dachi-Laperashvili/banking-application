package com.example.bank.Transaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class TransactionRepository {
    private String dbUrl = "jdbc:mysql://127.0.0.1:3306/bank";
    private String dbUser = "root";
    private String dbPassword = System.getenv("DB_PASSWORD");

    public void add(Transaction transaction){
        try(Connection connection= DriverManager.getConnection(dbUrl,dbUser,dbPassword);
                PreparedStatement statement =
                        connection.prepareStatement("INSERT INTO transactions (from_id,to_id,amount,date) VALUES (?, ? , ?, ?)")){
            statement.setLong(1, transaction.getFromId());
            statement.setLong(2, transaction.getToId());
            statement.setBigDecimal(3,transaction.getAmount());
            statement.setTimestamp(4, Timestamp.valueOf(transaction.getDate()));

            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
