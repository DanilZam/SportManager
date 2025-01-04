module org.app.sportmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;
    requires spring.security.crypto;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;



    opens org.app.sportmanager to javafx.fxml;
    opens org.app.sportmanager.controllers to javafx.fxml;
    opens org.app.sportmanager.models to javafx.base;
    opens org.app.sportmanager.fx_nodes to javafx.base;


    exports org.app.sportmanager.models;
    exports org.app.sportmanager;
    exports org.app.sportmanager.controllers to javafx.fxml;
}