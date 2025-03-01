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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
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
    private AnchorPane content;
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
    @FXML
    private VBox transactionsContainer;

    private final SessionManager session = SessionManager.getInstance();
    private final AccountRepository accountRepository = new AccountRepository();
    private final UserRepository userRepository = new UserRepository();
    private final TransactionRepository transactionRepository = new TransactionRepository();


    //   Initializing

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
        displayAccountInfo();
        displayLatestTransactions();
        startAutoRefresh();
    }

    @FXML
    public void init(){
        DateTimeFormatter formatObj = DateTimeFormatter.ofPattern("HH:mm MMM dd, yyyy");
        date.setText(LocalDateTime.now().format(formatObj));
        welcomeText.setText("Hello, " + session.getCurrentUser().getFirstName());
        System.out.println(session.getCurrentUser());
    }

    // Displaying information
    public void displayAccountInfo(){
        List<BankAccount> accounts= accountRepository.findAccountsByUserId(session.getCurrentUser().getId());

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

    public void displayLatestTransactions(){
        transactionsContainer.getChildren().clear();

        List<Transaction> transactions = transactionRepository.getLatestTransactions(session.getCurrentUser().getPersonalId());

        for(Transaction transaction: transactions){
            User fromUser = userRepository.findUserByPersonalId(transaction.getFromId());
            User toUser = userRepository.findUserByPersonalId(transaction.getToId());
            Label transactionLabel = new Label(
                    (transaction.getFromId() == session.getCurrentUser().getPersonalId() ? "Sent to: " + toUser.getFullName()  : "Received from: " + fromUser.getFullName()) +
                         " | $" + transaction.getAmount().setScale(2, RoundingMode.HALF_UP) +
                            " | " + transaction.getDate().format(DateTimeFormatter.ofPattern("MMM dd, HH:mm"))
            );
            transactionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #343a40;");
            transactionsContainer.getChildren().add(transactionLabel);
        }
    }

    // functionality for creating transactions

    public void sendMoney(){
        // clearing error messages
        payeeIdError.setText("");
        amountError.setText("");

        // checking if any fields are empty
        if(payeeIdField.getText().trim().isEmpty()){
            payeeIdError.setText("Payee ID is required!");
        }

        if(amountField.getText().trim().isEmpty()){
            amountError.setText("amount is required!");
        }

        // making sure that ID and amount only contains numbers and ID is 11 characters long
        if(!payeeIdField.getText().matches("[0-9]+") || payeeIdField.getText().length() != 11){
            payeeIdError.setText("Invalid ID!");
            return;
        }
        if(!amountField.getText().matches("[0-9]+") ){
            amountError.setText("Invalid amount!");
            return;
        }

        if(payeeIdField.getText().trim().equals(String.valueOf(session.getCurrentUser().getPersonalId()))){
            payeeIdError.setText("Payee ID can't be your ID!");
            return;
        }

        // retrieving payee user from database
        long payeeId = Long.parseLong(payeeIdField.getText());

        User payee = userRepository.findUserByPersonalId(payeeId);

        // creating transaction if payee user exists
        if(payee != null){
            // find sender user's checking account
            BigDecimal amount = new BigDecimal(amountField.getText());
            BankAccount senderAcc = null;

            List<BankAccount> senderAccounts = accountRepository.findAccountsByUserId(session.getCurrentUser().getId());

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

            List<BankAccount> accounts = accountRepository.findAccountsByUserId(payee.getId());

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

    //  auto refresh
    public void startAutoRefresh() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event ->{
            displayAccountInfo();
            displayLatestTransactions();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    // Sidebar buttons functionality

    @FXML
    public void openTransactions(ActionEvent event) throws IOException{
        try {
            NavigationUtil.navigate("/com/example/bank/transactions.fxml",event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openAccounts(ActionEvent event) throws IOException{
        try {
            NavigationUtil.navigate("/com/example/bank/accounts.fxml",event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout(ActionEvent event) throws IOException {
        session.logout();
        NavigationUtil.navigate("/com/example/bank/login.fxml",event);
    }

}
