package com.example.bank.Dashboard;

import com.example.bank.User.SessionManager;
import com.example.bank.Utilities.NavigationUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private Label welcomeText;
    @FXML
    private Label date;
    private final SessionManager session = SessionManager.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
    }

    @FXML
    public void init(){
        DateTimeFormatter formatObj = DateTimeFormatter.ofPattern("HH:mm MMM dd, yyyy");
        date.setText(LocalDateTime.now().format(formatObj));
        welcomeText.setText("Hello, " + session.getCurrentUser().getFirstName());
        System.out.println(session.getCurrentUser());
    }

    public void logout(ActionEvent event) throws IOException {
        session.logout();
        NavigationUtil.navigate("/com/example/bank/login.fxml",event);
    }

}
