package com.example.bank.User;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserRepository {

    private String dbUrl = "jdbc:mysql://127.0.0.1:3306/bank";
    private String dbUser = "root";
    private String dbPassword = System.getenv("DB_PASSWORD");

    public void add(User user){
        try(Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO users (fullName,password,personal_id) VALUES (?, ?, ?)")){

            statement.setString(1, user.getFullName());
            statement.setString(2, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            statement.setLong(3, user.getPersonalId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public User findAccountByPersonalId(long personal_id){
        try(Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE personal_id = ?")){

            statement.setLong(1,personal_id);

            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    User user = new User(resultSet.getString("fullName"),resultSet.getLong("personal_id"),resultSet.getString("password"));
                    return user;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
