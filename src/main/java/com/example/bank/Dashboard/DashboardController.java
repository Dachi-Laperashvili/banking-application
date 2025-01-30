package com.example.bank.Dashboard;

import com.example.bank.Account.AccountRepository;
import com.example.bank.Account.AccountType;
import com.example.bank.Account.BankAccount;
import com.example.bank.User.SessionManager;
import com.example.bank.Utilities.NavigationUtil;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.io.IOException;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
    @FXML
    private Label savingsBalance;
    @FXML
    private Label savingsAccountId;
    private final SessionManager session = SessionManager.getInstance();
    private final AccountRepository accountRepository = new AccountRepository();
    private Timeline timeline;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
        displayAccountInfo();
        startAutoRefresh();
    }

    @FXML
    public void init(){
        DateTimeFormatter formatObj = DateTimeFormatter.ofPattern("HH:mm MMM dd, yyyy");
        date.setText(LocalDateTime.now().format(formatObj));
        welcomeText.setText("Hello, " + session.getCurrentUser().getFirstName());
        System.out.println(session.getCurrentUser());
    }

    public void displayAccountInfo(){
        List<BankAccount> accounts= accountRepository.findAccountByUserId(session.getCurrentUser().getId());

        for(BankAccount account: accounts){
            String accountId = account.getAccountId().toString();
            int length = accountId.length();

            if(account.getAccountType() == AccountType.CHECKING_ACCOUNT) {
                checkingBalance.setText("$" + account.getBalance().setScale(2, RoundingMode.HALF_UP));
                checkingAccountId.setText("*** *** *** *** " + accountId.substring(length - 4));
            }else if(account.getAccountType() == AccountType.SAVINGS_ACCOUNT){
                savingsBalance.setText("$" + account.getBalance().setScale(2, RoundingMode.HALF_UP));
                savingsAccountId.setText("*** *** *** *** " + accountId.substring(length - 4));
            }
        }
    }
    public void startAutoRefresh() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> displayAccountInfo()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void logout(ActionEvent event) throws IOException {
        session.logout();
        NavigationUtil.navigate("/com/example/bank/login.fxml",event);
    }

}
