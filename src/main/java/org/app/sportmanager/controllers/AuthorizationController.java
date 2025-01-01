package org.app.sportmanager.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.app.sportmanager.HashUtil;
import org.app.sportmanager.UserSession;
import org.app.sportmanager.models.User;
import org.app.sportmanager.repositories.UserRepository;

import java.io.IOException;

public class AuthorizationController {

    @FXML
    public Button signInButton;

    @FXML
    public Button signInEnterButton;

    @FXML
    public Button signUpButton;

    @FXML
    public Button signUpEnterButton;

    @FXML
    public PasswordField signin_password_field;

    @FXML
    public TextField signin_username_field;

    @FXML
    public PasswordField signup_conf_password_field;

    @FXML
    public PasswordField signup_password_field;

    @FXML
    public TextField signup_username_field;

    @FXML
    public AnchorPane signInPane;

    @FXML
    public AnchorPane signUpPane;

    @FXML
    public Label signin_error_label;

    @FXML
    public Label signup_error_label;

    @FXML
    public void initialize() {
        signUpPane.setVisible(false);

        signInButton.setOnAction(event -> {
            signInButton.getStyleClass().removeAll("def_button", "pressed_button");
            signInButton.getStyleClass().add("pressed_button");

            signUpButton.getStyleClass().removeAll("def_button", "pressed_button");
            signUpButton.getStyleClass().add("def_button");

            signInPane.setVisible(true);
            signUpPane.setVisible(false);
        });

        signInEnterButton.setOnAction(event -> {
            UserRepository userRepository = new UserRepository();
            String username = signin_username_field.getText();
            String password = signin_password_field.getText();

            System.out.println(username + "   " + password);

            if (username.equals("") || password.isEmpty()){
                signin_error_label.setText("Empty field!");
                return;
            }
            else if (username.length() < 4) {
                signin_error_label.setText("Name must be >4 symbols");
                return;
            }
            else if (password.length() < 4) {
                signin_error_label.setText("Password must be >4 symbols");
                return;
            }

            if(userRepository.signIn(username, password)){
                try {
                    // Загружаем FXML нового окна
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main-view.fxml"));
                    Parent root = loader.load();

                    // Получаем Stage из события
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                    // Создаем и настраиваем новое окно
                    Stage newStage = new Stage();
                    newStage.setScene(new Scene(root));
                    newStage.setTitle("Sport Manager"); // Заголовок нового окна
                    newStage.show();

                    // Закрываем старое окно
                    currentStage.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Open win");
            }
            else{
                signin_error_label.setText("Wrong name or password");
                return;
            }

        });

        signUpButton.setOnAction(event -> {
            signUpButton.getStyleClass().removeAll("def_button", "pressed_button");
            signUpButton.getStyleClass().add("pressed_button");

            signInButton.getStyleClass().removeAll("def_button", "pressed_button");
            signInButton.getStyleClass().add("def_button");

            signInPane.setVisible(false);
            signUpPane.setVisible(true);
        });

        signUpEnterButton.setOnAction(event -> {
            UserRepository userRepository = new UserRepository();
            String username = signup_username_field.getText();
            String password = signup_password_field.getText();
            String conf_password = signup_conf_password_field.getText();

            if (username.isEmpty() || password.isEmpty() || conf_password.isEmpty()){
                signup_error_label.setText("Empty field!");
                return;
            }
            else if (username.length() < 4) {
                signup_error_label.setText("Name must be >4 symbols");
                return;
            }
            else if (password.length() < 4) {
                signup_error_label.setText("Password must be >4 symbols");
                return;
            }
            else if (!password.equals(conf_password)) {
                signup_error_label.setText("Password hasn't been confirmed!");
                return;
            }

            if (userRepository.checkUserExist(username)){
                signup_error_label.setText("Name is taken");
            }
            else{
                Long id = userRepository.addUser(username, password);
                User newUser = new User(id, username, HashUtil.hashPassword(password));
                UserSession.getInstance().setCurrentUser(newUser);

                try {
                    // Загружаем FXML нового окна
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main-view.fxml"));
                    Parent root = loader.load();

                    // Получаем Stage из события
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                    // Создаем и настраиваем новое окно
                    Stage newStage = new Stage();
                    newStage.setScene(new Scene(root));
                    newStage.setTitle("Sport Manager"); // Заголовок нового окна
                    newStage.show();

                    // Закрываем старое окно
                    currentStage.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("Open win");
                return;
            }

        });
    }
}

