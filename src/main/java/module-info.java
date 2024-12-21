module org.app.sportmanager {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.app.sportmanager to javafx.fxml;
    exports org.app.sportmanager;
}