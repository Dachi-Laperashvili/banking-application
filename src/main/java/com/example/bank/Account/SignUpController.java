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

public class SignUpController {
    private AccountRepository accountRepository;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField id;
    @FXML
    private PasswordField password;

    public SignUpController(){
        this.accountRepository = new AccountRepository();
    }

    @FXML
    public void register(){

        //  checking if any fields are empty
        if(firstName.getText().isEmpty() || lastName.getText().isEmpty() || password.getText().isEmpty()){
            System.out.println("All fields are required!");
            return;
        }

        // making sure that ID only contains numbers and is 11 characters long
        if(!id.getText().matches("[0-9]+") || id.getText().length() != 11 ){
            System.out.println("Invalid ID");
            return;
        }

        // checking if password length is 8 characters long or more
        if(password.getText().length() < 8){
            System.out.println("Password should be at least 8 characters long!");
            return;
        }


        Account newAcc = new Account(firstName.getText(),lastName.getText(),Long.parseLong(id.getText()),password.getText());

        // creating new account if account with that id doesn't exist in array list
        if(accountRepository.findAccountByPersonalId(newAcc.getPersonalId()) == null){
            accountRepository.add(newAcc);
            System.out.println("Account created successfully");
            System.out.println(accountRepository.findAccountByPersonalId(newAcc.getPersonalId()));
        }else{
            System.out.println("Account with that ID already exists");
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
