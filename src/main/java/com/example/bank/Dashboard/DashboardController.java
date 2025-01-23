package com.example.bank.Dashboard;

import com.example.bank.User.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private Label welcomeText;
    private SessionManager session = SessionManager.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
    }

    @FXML
    public void init(){
        welcomeText.setText("Hello, " + session.getCurrentUser().getFirstName());
        System.out.println(session.getCurrentUser());
    }

    public void logout(ActionEvent event) throws IOException {
        session.logout();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/bank/login.fxml"));

        Stage stage =(Stage) ((Node)event.getSource()).getScene().getWindow();

        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

}
