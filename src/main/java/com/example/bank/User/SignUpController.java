package com.example.bank.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpController {
    private UserRepository userRepository;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField id;
    @FXML
    private PasswordField password;
    @FXML
    private Label firstNameError;
    @FXML
    private Label lastNameError;
    @FXML
    private Label idError;
    @FXML
    private Label passwordError;

    public SignUpController(){
        this.userRepository = new UserRepository();
    }

    @FXML
    public void register(){
        // clearing out errors
        firstNameError.setText("");
        lastNameError.setText("");
        idError.setText("");
        passwordError.setText("");

        //  checking if any fields are empty
        if(firstName.getText().isEmpty()){
            firstNameError.setText("First name is required!");
        }

        if(lastName.getText().isEmpty()){
            lastNameError.setText("Last name is required!");
        }

        if(password.getText().isEmpty()){
            passwordError.setText("Password is required!");
        }

        // making sure that ID only contains numbers and is 11 characters long
        if(!id.getText().matches("[0-9]+") || id.getText().length() != 11 ){
            idError.setText("Invalid ID!");
            return;
        }

        // checking if password length is 8 characters long or more
        if(password.getText().length() < 8){
            passwordError.setText("Password should be at least 8 characters long!");
            return;
        }


        User newAcc = new User(firstName.getText(),lastName.getText(),Long.parseLong(id.getText()),password.getText());

        // creating new account if account with that id doesn't exist in array list
        if(userRepository.findAccountByPersonalId(newAcc.getPersonalId()) == null){
            userRepository.add(newAcc);
            System.out.println("Account created successfully");
            System.out.println(userRepository.findAccountByPersonalId(newAcc.getPersonalId()));
        }else{
            idError.setText("Account with that ID already exists");
        }
    }

    //  displaying login fxml page when clicking on "already have an account?" button
    @FXML
    public void handleLoginBtn(ActionEvent e) throws IOException {
        System.out.println("Trying to load: " + getClass().getResource("/com/example/bank/login.fxml"));

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/bank/login.fxml"));

        Stage stage =(Stage) ((Node)e.getSource()).getScene().getWindow();

        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
}
