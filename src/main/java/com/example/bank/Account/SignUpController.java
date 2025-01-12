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
import java.util.ArrayList;

public class SignUpController {
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField id;
    @FXML
    private PasswordField password;
    private ArrayList<Account> accounts = new ArrayList<>();

    @FXML
    public void register(){
        boolean idExists = false;

        if(!id.getText().matches("[0-9]*")){
            System.out.println("Invalid ID");
            return;
        }

        for(Account account: accounts){
            if(account.getId().equals(id.getText())){
               idExists = true;
               break;
            }
        }

        if(idExists){
            accounts.add(new Account(firstName.getText(),lastName.getText(),id.getText(),password.getText()));
        }else{
            System.out.println("Account with that ID number already exists!");
        }
    }

    @FXML
    public void handleLoginBtn(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));

        Stage stage =(Stage) ((Node)e.getSource()).getScene().getWindow();

        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
}
