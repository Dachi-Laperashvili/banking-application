package com.example.bank.Account;

import com.example.bank.User.SessionManager;
import com.example.bank.Utilities.NavigationUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class AccountController {
    private SessionManager session = SessionManager.getInstance();

    // Sidebar buttons functionality

    @FXML
    public void openDashboard(ActionEvent event) throws IOException {
        try {
            NavigationUtil.navigate("/com/example/bank/dashboard.fxml",event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openTransactions(ActionEvent event) throws IOException{
        try {
            NavigationUtil.navigate("/com/example/bank/transactions.fxml",event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout(ActionEvent event) throws IOException {
        session.logout();
        NavigationUtil.navigate("/com/example/bank/login.fxml",event);
    }
}
