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
            PreparedStatement statement = connection.prepareStatement("INSERT INTO users (firstName,lastName,password,personal_id) VALUES (?, ?, ?, ?)",PreparedStatement.RETURN_GENERATED_KEYS)){

            statement.setString(1,user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            statement.setLong(4, user.getPersonalId());
            statement.executeUpdate();

            try(ResultSet generatedKeys = statement.getGeneratedKeys()){
                if(generatedKeys.next()){
                    user.setId(generatedKeys.getLong(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public User findUserByPersonalId(long personal_id){
        try(Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE personal_id = ?")){

            statement.setLong(1,personal_id);

            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    User user = new User(resultSet.getString("firstName"),resultSet.getString("lastName"),
                            resultSet.getLong("personal_id"),resultSet.getString("password"));
                    user.setId(resultSet.getLong("id"));
                    return user;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public User findUserById(long id){
        try(Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id = ?")){

            statement.setLong(1,id);

            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    User user = new User(resultSet.getString("firstName"),resultSet.getString("lastName"),
                            resultSet.getLong("personal_id"),resultSet.getString("password"));
                    user.setId(resultSet.getLong("id"));
                    return user;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
