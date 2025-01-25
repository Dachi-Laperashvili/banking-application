package com.example.bank.Dashboard;

import com.example.bank.Account.AccountRepository;
import com.example.bank.Account.BankAccount;
import com.example.bank.User.SessionManager;
import com.example.bank.Utilities.NavigationUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private Label welcomeText;
    @FXML
    private Label date;
    @FXML
    private Label checkingBalance;
    @FXML
    private Label checkingAccountId;
    private final SessionManager session = SessionManager.getInstance();
    private final AccountRepository accountRepository = new AccountRepository();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
        displayAccountInfo();
    }

    @FXML
    public void init(){
        DateTimeFormatter formatObj = DateTimeFormatter.ofPattern("HH:mm MMM dd, yyyy");
        date.setText(LocalDateTime.now().format(formatObj));
        welcomeText.setText("Hello, " + session.getCurrentUser().getFirstName());
        System.out.println(session.getCurrentUser());
    }

    public void displayAccountInfo(){
        BankAccount bankAccount = accountRepository.findAccountByUserId(session.getCurrentUser().getId());
        String accountId = bankAccount.getAccountId().toString();
        int length = accountId.length();
        checkingBalance.setText("$" + bankAccount.getBalance().setScale(2, RoundingMode.HALF_UP));
        checkingAccountId.setText("*** *** *** *** " + accountId.substring(length - 4));
    }

    public void logout(ActionEvent event) throws IOException {
        session.logout();
        NavigationUtil.navigate("/com/example/bank/login.fxml",event);
    }

}
