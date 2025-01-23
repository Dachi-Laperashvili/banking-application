package com.example.bank.User;

import com.example.bank.Dashboard.DashboardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

public class LoginController {
    private UserRepository userRepository;
    @FXML
    private TextField idField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label idErrorLabel;
    @FXML
    private Label passwordErrorLabel;

    public LoginController(){
        userRepository = new UserRepository();
    }

    @FXML
    private void handleLogin(ActionEvent event) throws IOException{
    //  clearing error messages
        idErrorLabel.setText("");
        passwordErrorLabel.setText("");

        SessionManager session = SessionManager.getInstance();

        //  checking if any fields are empty

        if(idField.getText().isEmpty()){
            idErrorLabel.setText("ID is required!");
        }

        if(passwordField.getText().isEmpty()){
            passwordErrorLabel.setText("Password is required!");
        }

        // making sure that ID only contains numbers
        if(!idField.getText().matches("[0-9]+")){
            idErrorLabel.setText("Invalid ID");
            return;
        }

        User user = userRepository.findAccountByPersonalId(Long.parseLong(idField.getText()));
        // if account exists logging user in using session manager class
        if(user != null){
//            account.getPassword().equals(passwordField.getText()
            if(BCrypt.checkpw(passwordField.getText(), user.getPassword())){
                session.login(user);
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/bank/dashboard.fxml"));

                Stage stage =(Stage) ((Node)event.getSource()).getScene().getWindow();

                Scene scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
                stage.show();
                System.out.println(session.getCurrentUser());
                System.out.println("Successfully logged in!");
            }else {
                passwordErrorLabel.setText("Incorrect password!");
            }
        }else{
            idErrorLabel.setText("Account with that ID doesn't exist!");
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
