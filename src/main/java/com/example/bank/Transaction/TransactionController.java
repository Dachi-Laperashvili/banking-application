package com.example.bank.Transaction;

import com.example.bank.User.SessionManager;
import com.example.bank.User.User;
import com.example.bank.User.UserRepository;
import com.example.bank.Utilities.NavigationUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.geometry.Insets;

public class TransactionController {
    @FXML
    private VBox transactionsContainer;

    private final SessionManager session = SessionManager.getInstance();
    private final UserRepository userRepository = new UserRepository();
    private final TransactionRepository transactionRepository = new TransactionRepository();

    @FXML
    public void initialize() {
        loadTransactions();
    }

    private void loadTransactions() {
        transactionsContainer.getChildren().clear();

        List<Transaction> transactions = transactionRepository.getAll(session.getCurrentUser().getPersonalId());

        for (Transaction transaction : transactions) {
            User fromUser = userRepository.findUserByPersonalId(transaction.getFromId());
            User toUser = userRepository.findUserByPersonalId(transaction.getToId());
            Label transactionLabel = new Label(
                    (transaction.getFromId() == session.getCurrentUser().getPersonalId() ? "Sent to: " + toUser.getFullName()  : "Received from: " + fromUser.getFullName())
                            + " | $" + transaction.getAmount().setScale(2, RoundingMode.HALF_UP) +
                            " | " + transaction.getDate().format(DateTimeFormatter.ofPattern("MMM dd, HH:mm"))
            );

            transactionLabel.setStyle("-fx-font-size: 17px; " +
                    "-fx-text-fill: #333; " +
                    "-fx-background-color: #f9f9f9; " +
                    "-fx-border-width: 1px; " +
                    "-fx-background-radius: 5px; ");

            transactionLabel.setPadding(new Insets(10));
            VBox.setMargin(transactionLabel, new Insets(10));
            transactionsContainer.getChildren().add(transactionLabel);
        }
    }

    @FXML
    public void openDashboard(ActionEvent event) throws IOException {
        try {
            NavigationUtil.navigate("/com/example/bank/dashboard.fxml",event);
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
