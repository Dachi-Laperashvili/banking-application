package com.example.bank.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<Transaction> getLatestTransactions(long userId){
        List<Transaction> transactions = new ArrayList<>();
        try(Connection connection= DriverManager.getConnection(dbUrl,dbUser,dbPassword);
            PreparedStatement statement =
                    connection.prepareStatement("SELECT * FROM transactions WHERE to_id = ? OR from_id = ? ORDER BY date DESC LIMIT 5")){
            statement.setLong(1, userId);
            statement.setLong(2, userId);

            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    Transaction transaction = new Transaction(
                            resultSet.getLong("to_id"),
                            resultSet.getLong("from_id"),
                            resultSet.getBigDecimal("amount"),
                            resultSet.getTimestamp("date").toLocalDateTime()
                    );

                    transactions.add(transaction);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }
}
