package com.example.bank.Account;

import com.example.bank.User.SessionManager;
import com.example.bank.Utilities.NavigationUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AccountController implements Initializable {
    private final SessionManager session = SessionManager.getInstance();
    private final AccountRepository accountRepository = new AccountRepository();
    @FXML
    private Text CheckingNumber;
    @FXML
    private Text CheckingBalance;
    @FXML
    private Text SavingsNumber;
    @FXML
    private Text SavingsBalance;

    @FXML
    private TextField amountForSavings;
    @FXML
    private TextField amountForChecking;
    @FXML
    private Label savingsAmountError;
    @FXML
    private Label checkingAmountError;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayInfo();
    }

    // displaying checking and savings account details
    public void displayInfo(){
        List<BankAccount> accounts = accountRepository.findAccountsByUserId(session.getCurrentUser().getId());

        for(BankAccount account: accounts){
            if(account.getAccountType() == AccountType.CHECKING_ACCOUNT){
                CheckingNumber.setText(account.getAccountId().toString());
                CheckingBalance.setText("$" + account.getBalance().setScale(2, RoundingMode.HALF_UP));
            }else if(account.getAccountType() == AccountType.SAVINGS_ACCOUNT){
                SavingsNumber.setText(account.getAccountId().toString());
                SavingsBalance.setText("$" + account.getBalance().setScale(2, RoundingMode.HALF_UP));
            }
        }
    }
    // transfering funds

    public void transferToSavings(){
        if(amountForSavings.getText().trim().isEmpty()){
            savingsAmountError.setText("amount is required!");
            return;
        }

        if(!amountForSavings.getText().matches("[0-9]+") ){
            savingsAmountError.setText("Invalid amount!");
            return;
        }

        List<BankAccount> accounts = accountRepository.findAccountsByUserId(session.getCurrentUser().getId());

        for(BankAccount account: accounts){
            BigDecimal amount = new BigDecimal(amountForSavings.getText());
            if(account.getAccountType() == AccountType.CHECKING_ACCOUNT){
                if(account.getBalance().compareTo(amount) < 0){
                    savingsAmountError.setText("Insufficient balance!");
                    return;
                }
                // deducting amount from checking account
                account.setBalance(account.getBalance().subtract(amount));
                accountRepository.updateAccountBalance(account);

            }

            if(account.getAccountType() == AccountType.SAVINGS_ACCOUNT){
                account.setBalance(account.getBalance().add(amount));
                accountRepository.updateAccountBalance(account);
            }
        }
        displayInfo();
    }

    public void transferToCheckingAcc(){
        if(amountForChecking.getText().trim().isEmpty()){
            checkingAmountError.setText("amount is required!");
            return;
        }

        if(!amountForChecking.getText().matches("[0-9]+") ){
            checkingAmountError.setText("Invalid amount!");
            return;
        }

        List<BankAccount> accounts = accountRepository.findAccountsByUserId(session.getCurrentUser().getId());

        for(BankAccount account: accounts){
            BigDecimal amount = new BigDecimal(amountForChecking.getText());
            if(account.getAccountType() == AccountType.SAVINGS_ACCOUNT){
                if(account.getBalance().compareTo(amount) < 0){
                    checkingAmountError.setText("Insufficient balance!");
                    return;
                }
                // deducting amount from checking account
                account.setBalance(account.getBalance().subtract(amount));
                accountRepository.updateAccountBalance(account);

            }

            if(account.getAccountType() == AccountType.CHECKING_ACCOUNT){
                account.setBalance(account.getBalance().add(amount));
                accountRepository.updateAccountBalance(account);
            }
        }
        displayInfo();
    }

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
