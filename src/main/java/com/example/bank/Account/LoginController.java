package com.example.bank.Account;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField idField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin(){
        System.out.println("Error, login functionality yet to be created");
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
