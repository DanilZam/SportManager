module org.app.sportmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires spring.security.crypto;


    opens org.app.sportmanager to javafx.fxml;
    opens org.app.sportmanager.controllers to javafx.fxml;
    exports org.app.sportmanager;
    exports org.app.sportmanager.controllers to javafx.fxml;
}