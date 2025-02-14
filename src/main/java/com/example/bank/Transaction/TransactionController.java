package com.example.bank.Transaction;

import com.example.bank.User.SessionManager;
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

public class TransactionController {
    @FXML
    private VBox transactionsContainer;

    private final SessionManager session = SessionManager.getInstance();
    private final TransactionRepository transactionRepository = new TransactionRepository();

    @FXML
    public void initialize() {
        loadTransactions();
    }

    private void loadTransactions() {
        transactionsContainer.getChildren().clear();

        List<Transaction> transactions = transactionRepository.getAll(session.getCurrentUser().getPersonalId());

        for (Transaction transaction : transactions) {
            Label transactionLabel = new Label(
                    (transaction.getFromId() == session.getCurrentUser().getPersonalId() ? "Sent to: " : "Received from: ") +
                            transaction.getToId() + " | $" + transaction.getAmount().setScale(2, RoundingMode.HALF_UP) +
                            " | " + transaction.getDate().format(DateTimeFormatter.ofPattern("MMM dd, HH:mm"))
            );
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

    public void logout(ActionEvent event) throws IOException {
        session.logout();
        NavigationUtil.navigate("/com/example/bank/login.fxml",event);
    }
}
