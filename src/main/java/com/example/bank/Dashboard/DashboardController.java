package com.example.bank.Dashboard;

import com.example.bank.Account.AccountRepository;
import com.example.bank.Account.AccountType;
import com.example.bank.Account.BankAccount;
import com.example.bank.Transaction.Transaction;
import com.example.bank.Transaction.TransactionRepository;
import com.example.bank.User.SessionManager;
import com.example.bank.User.User;
import com.example.bank.User.UserRepository;
import com.example.bank.Utilities.NavigationUtil;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.io.IOException;
import java.math.BigDecimal;
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
    @FXML
    private TextField payeeIdField;
    @FXML
    private TextField amountField;
    @FXML
    private Label payeeIdError;
    @FXML
    private Label amountError;

    private final SessionManager session = SessionManager.getInstance();
    private final AccountRepository accountRepository = new AccountRepository();
    private final UserRepository userRepository = new UserRepository();
    private final TransactionRepository transactionRepository = new TransactionRepository();


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

    public void sendMoney(){
        BigDecimal amount = new BigDecimal(amountField.getText());

        // clearing error messages
        payeeIdError.setText("");
        amountError.setText("");

        // checking if any fields are empty
        if(payeeIdField.getText().isEmpty()){
            payeeIdError.setText("Payee ID is required!");
        }

        if(amountField.getText().isEmpty()){
            amountError.setText("amount is required!");
        }

        // making sure that ID only contains numbers and is 11 characters long
        if(!payeeIdField.getText().matches("[0-9]+") || payeeIdField.getText().length() != 11 ){
            payeeIdError.setText("Invalid ID!");
            return;
        }

        if(payeeIdField.getText().equals(session.getCurrentUser().getId().toString())){
            return;
        }

        // find sender user's checking account
        BankAccount senderAcc = null;

        List<BankAccount> senderAccounts = accountRepository.findAccountByUserId(session.getCurrentUser().getId());

        for(BankAccount account: senderAccounts){
            if(account.getAccountType() == AccountType.CHECKING_ACCOUNT){
                senderAcc = account;
            }
        }

        // check if user has enough balance
        if(senderAcc != null && senderAcc.getBalance().compareTo(amount) < 0){
            amountError.setText("Insufficient balance!");
            return;
        }

        // deduct amount from senders account
        senderAcc.setBalance(senderAcc.getBalance().subtract(amount));
        accountRepository.updateAccountBalance(senderAcc);

        // retrieving payee user from database
        long payeeId = Long.parseLong(payeeIdField.getText());

        User payee = userRepository.findUserByPersonalId(payeeId);

        // creating transaction if payee user exists
        if(payee != null){

            List<BankAccount> accounts = accountRepository.findAccountByUserId(payee.getId());

        //  finding payee's checking account and updating it in database
            for(BankAccount account: accounts){
                System.out.println(account);
                if(account.getAccountType() == AccountType.CHECKING_ACCOUNT){
                    account.setBalance(account.getBalance().add(amount));
                    accountRepository.updateAccountBalance(account);
                }
            }

            // saving transaction
            Transaction transaction = new Transaction(payeeId,session.getCurrentUser().getPersonalId(),
                    amount, LocalDateTime.now());
            transactionRepository.add(transaction);
        }else{
            payeeIdError.setText("Account with that ID doesn't exists");
        }
    }
    public void startAutoRefresh() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> displayAccountInfo()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void logout(ActionEvent event) throws IOException {
        session.logout();
        NavigationUtil.navigate("/com/example/bank/login.fxml",event);
    }

}
