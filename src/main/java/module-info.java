module com.example.bank {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires jbcrypt;
    requires mysql.connector.j;

    opens com.example.bank to javafx.fxml;
    exports com.example.bank;
    exports com.example.bank.User;
    opens com.example.bank.User to javafx.fxml;
    exports com.example.bank.Dashboard;
    opens com.example.bank.Dashboard to javafx.fxml;
}