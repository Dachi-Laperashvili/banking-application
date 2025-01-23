package com.example.bank.Utilities;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public final class NavigationUtil {

    private NavigationUtil(){}

//    reusable method for switching fxml scenes
    public static void navigate(String path, ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(NavigationUtil.class.getResource(path));

        Stage stage =(Stage) ((Node)event.getSource()).getScene().getWindow();

        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
}
