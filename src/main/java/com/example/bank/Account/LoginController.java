package com.example.bank.Account;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

public class LoginController {
    private AccountRepository accountRepository;
    @FXML
    private TextField idField;
    @FXML
    private PasswordField passwordField;

    public LoginController(){
        accountRepository = new AccountRepository();
    }

    @FXML
    private void handleLogin(){
        SessionManager session = SessionManager.getInstance();

        //  checking if any fields are empty
        if(passwordField.getText().isEmpty() || idField.getText().isEmpty()){
            System.out.println("All fields are required!");
            return;
        }

        // making sure that ID only contains numbers
        if(!idField.getText().matches("[0-9]+")){
            System.out.println("Invalid ID");
            return;
        }

        Account account = accountRepository.findAccountByPersonalId(Long.parseLong(idField.getText()));
        // if account exists logging user in using session manager class
        if(account != null){
//            account.getPassword().equals(passwordField.getText()
            if(BCrypt.checkpw(passwordField.getText(),account.getPassword())){
                session.login(account);
                System.out.println(session.getCurrentAcc());
                System.out.println("Successfully logged in!");
            }else {
                System.out.println("Invalid password!");
            }
        }else{
            System.out.println("Account with that ID doesn't exist!");
        }
    }

    //  displaying signup fxml file when clicking on sign up button
    @FXML
    private void handleSignUp(ActionEvent event) throws IOException{
        System.out.println("Trying to load: " + getClass().getResource("/com/example/bank/signup.fxml"));

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/bank/signup.fxml"));

        Stage stage =(Stage) ((Node)event.getSource()).getScene().getWindow();

        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
}
