package org.app.sportmanager.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.transform.Scale;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.app.sportmanager.SVGLoader;
import org.app.sportmanager.UserSession;
import org.app.sportmanager.fx_nodes.ResultRow;
import org.app.sportmanager.fx_nodes.TrainingTableRow;
import org.app.sportmanager.models.Article;
import org.app.sportmanager.models.Exercise;
import org.app.sportmanager.models.Training;
import org.app.sportmanager.models.User;
import org.app.sportmanager.repositories.ArticleRepository;
import org.app.sportmanager.repositories.ExerciseRepository;
import org.app.sportmanager.repositories.TrainingRepository;
import org.app.sportmanager.repositories.UserRepository;


import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class MainController {

    private User user;

    private final String CONNECTION_URL = "mongodb://localhost:27017";
    private final String DB_NAME = "sportmanager";
    private final String EXERCISES_COLLECTION = "exercises";
    private final String TRAININGS_COLLECTION = "trainings";
    private final String ARTICLES_COLLECTION = "articles";

    private ObservableList<TrainingTableRow> originalData = FXCollections.observableArrayList();
    SVGLoader loader = new SVGLoader();

    private Article selectedArticle;

    private List<Region> panes;

    private boolean changesSaved = false;

    @FXML
    public Button add_newexcercise_button;

    @FXML
    public Button add_train_button;

    @FXML
    public Button articles_button;

    @FXML
    public Button calculators_button;

    @FXML
    public DatePicker end_datepicker;

    @FXML
    public AnchorPane exercises_anchorpane;

    @FXML
    public Button exercises_button;

    @FXML
    public TextField exercises_search_textfield;

    @FXML
    public TableView<Exercise> exercises_tableview;

    @FXML
    public VBox left_panel_vbox;

    @FXML
    public BorderPane main_borderpane;

    @FXML
    public TableColumn<Exercise, String> musclegroups_columns;

    @FXML
    public TextField muscles_search_textfield;

    @FXML
    public TableColumn<Exercise, String> name_column;

    @FXML
    public AnchorPane newtarin_anchorpane;

    @FXML
    public VBox newtrain_vbox;

    @FXML
    public Button search_exercises_button;

    @FXML
    public Button settings_button;

    @FXML
    public DatePicker start_datepicker;

    @FXML
    public AnchorPane trainings_anchorpane;

    @FXML
    public Button trainings_button;

    @FXML
    public TableView<TrainingTableRow> trainings_tableview;

    @FXML
    public Pane user_icon_pane;

    @FXML
    public Label user_label;

    @FXML
    public Button create_train_button;

    @FXML
    public DatePicker train_date_datepicker;

    @FXML
    public TextArea train_description_textarea;

    @FXML
    public TableColumn<?, ?> train_date_column;

    @FXML
    public TableColumn<?, ?> train_muscles_column;

    @FXML
    public TableColumn<?, ?> train_name_column;

    @FXML
    public Button search_train_button;

    @FXML
    public TextField search_train_name_textfield;

    @FXML
    public TextField search_train_muscle_textfield;

    @FXML
    public AnchorPane calculator_anchorpane;

    @FXML
    public Spinner<Integer> reps_spinner;

    @FXML
    public TableView<ResultRow> calc_tableview;

    @FXML
    public TableColumn<ResultRow, String> repsColumn;

    @FXML
    public TableColumn<ResultRow, String> brzyckiColumn;

    @FXML
    public TableColumn<ResultRow, String> epleyColumn;

    @FXML
    public TableColumn<ResultRow, String> landerColumn;

    @FXML
    public TableColumn<ResultRow, String> oconnerColumn;

    @FXML
    public AnchorPane articles_anchorpane;
    @FXML
    public AnchorPane create_article_anchorpane;

    @FXML
    public Button create_article_button;
    @FXML
    public TableView<Article> articles_tableview;
    @FXML
    public TableColumn<Article, String> article_name_column;
    @FXML
    public TableColumn<Article, String> article_date_column;

    @FXML
    public TextField article_title_textfield;

    @FXML
    public HTMLEditor article_htmleditor;

    @FXML
    public Button add_newarticle_button;

    @FXML
    public AnchorPane article_view_pane;

    @FXML
    public Label article_title_label;

    @FXML
    public WebView article_content_webview;

    @FXML
    public Button close_view_button;

    @FXML
    public Label article_date_label;

    @FXML
    public TextField edit_article_title_field;

    @FXML
    public HTMLEditor edit_article_content_htmleditor;

    @FXML
    public AnchorPane article_edit_pane;

    @FXML
    public Button save_article_button;

    @FXML
    public Button cancel_edit_button;

    @FXML
    public Button edit_article_button;

    @FXML
    public Button delete_article_button;

    @FXML
    public Button exit_create_article_pane;

    @FXML
    public Spinner<Double> weight_spinner;

    @FXML
    public Button exit_newtrain_button;

    @FXML
    public ImageView person_imageview;


    @FXML
    public TabPane settings_tabpane;
    @FXML
    public Button exit_settings1;

    @FXML
    public Button change_nickname_button;

    @FXML
    public Button chang_pass_button;

    @FXML
    public Button accept_newpass_button;

    @FXML
    public Button cancel_change_pass_button;

    @FXML
    public PasswordField newpass_field;

    @FXML
    public PasswordField accept_newpass_field;


    @FXML
    public Button delete_user_button;

    @FXML
    public Button exit_main_button;

    @FXML
    public TextField nickname_textfield;

    @FXML
    public Label trains_amount_label;

    @FXML
    public Button accept_newnickname_button;

    @FXML
    public Button cancel_change_nickname_button;



    @FXML
    public void initialize() {
        user = UserSession.getInstance().getCurrentUser();

        panes = Arrays.asList(
                trainings_anchorpane,
                newtarin_anchorpane,
                exercises_anchorpane,
                calculator_anchorpane,
                article_view_pane,
                articles_anchorpane,
                create_article_anchorpane,
                article_edit_pane,
                settings_tabpane
        );

        user_label.setText(user.getNickname());

        String pathSVG = loader.getStyleStr("settings.svg");
        if (pathSVG != null) {
            settings_button.setStyle(STR."-fx-shape: \"\{pathSVG}\"");
            //user_icon_region.setStyle(svgStyle);
        } else {
            System.out.println("Set string");
            settings_button.setText("Настройки");
        }


        person_imageview.setImage(new Image(getClass().getResource("/assets/user.png").toExternalForm()));


        article_content_webview.setMouseTransparent(true);

        loadTrainingsToTableView(trainings_tableview);
        originalData.addAll(trainings_tableview.getItems());

        exit_newtrain_button.setOnAction(event -> exitNewTrain());

        exercises_tableview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        start_datepicker.setOnAction(event -> applyFilters());
        end_datepicker.setOnAction(event -> applyFilters());
        search_train_name_textfield.setOnKeyReleased(event -> applyFilters());
        search_train_muscle_textfield.setOnKeyReleased(event -> applyFilters());


        trainings_anchorpane.setVisible(true);
        newtarin_anchorpane.setVisible(false);
        exercises_anchorpane.setVisible(false);
        calculator_anchorpane.setVisible(false);
        article_view_pane.setVisible(false);
        articles_anchorpane.setVisible(false);
        create_article_anchorpane.setVisible(false);
        article_edit_pane.setVisible(false);
        settings_tabpane.setVisible(false);

        weight_spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 999.0, 30.0, 1.0));
        reps_spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 999, 8, 1));

        exit_create_article_pane.setOnAction(event -> exitCreateArticle());

        create_article_button.setOnAction(event -> showCreateArticlePane());
        articles_button.setOnAction(event -> {
            ArticleRepository articleRepository = new ArticleRepository(CONNECTION_URL, DB_NAME, ARTICLES_COLLECTION);

            showOnlyPane(articles_anchorpane);

            article_name_column.setCellValueFactory(new PropertyValueFactory<>("title"));
            article_date_column.setCellValueFactory(article ->
                    new SimpleStringProperty(article.getValue().getDate().toString())
            );

            // Загрузка статей из MongoDB
            loadArticles(articleRepository);

        });


        // Обрабатываем выбор строки в таблице
        articles_tableview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                showArticleDetails(newValue);
                selectedArticle = newValue;
            }
            else {
                System.out.println("Error open article");
            }

        });

        edit_article_button.setOnAction(event -> {
            edit_article_title_field.setText(selectedArticle.getTitle());
            edit_article_content_htmleditor.setHtmlText(selectedArticle.getContent());

            article_edit_pane.setVisible(true);
            article_view_pane.setVisible(false);
            changesSaved = false;
        });

        save_article_button.setOnAction(event -> saveEditedArticle());

        delete_article_button.setOnAction(event -> deleteArticle());

        cancel_edit_button.setOnAction(event -> {

            if (!changesSaved){
                String result = confirmActionAlert("Сохранить?", "Хотите сохранить изменения перед закрытием?");
                if (result.equals("yes")){
                    saveEditedArticle();
                }
            }
            article_edit_pane.setVisible(false);
            changesSaved = false;
        });

        // Закрыть просмотр
        close_view_button.setOnAction(event -> closeArticleView());


        /*articles_tableview.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Article selectedArticle = articles_tableview.getSelectionModel().getSelectedItem();
                if (selectedArticle != null) {
                    showArticlePane(selectedArticle);
                }
            }
        });*/


        name_column.setCellValueFactory(new PropertyValueFactory<>("name"));
        musclegroups_columns.setCellValueFactory(cellData -> {
            // Преобразуем список мышечных групп в строку, разделенную запятыми
            String muscleGroups = String.join(", ", cellData.getValue().getMuscleGroups());
            return new javafx.beans.property.SimpleStringProperty(muscleGroups);
        });

        // Устанавливаем редактирование ячеек (если нужно)
        musclegroups_columns.setCellFactory(TextFieldTableCell.forTableColumn());


        trainings_button.setOnAction(event -> {
            showOnlyPane(trainings_anchorpane);
            loadTrainingsToTableView(trainings_tableview);
        });

        exercises_button.setOnAction(event -> {
            showOnlyPane(exercises_anchorpane);

            ExerciseRepository exerciseRepository = new ExerciseRepository(CONNECTION_URL, DB_NAME, EXERCISES_COLLECTION);

            List<Exercise> exercises = exerciseRepository.getAllExercises();

            ObservableList<Exercise> exerciseList = FXCollections.observableArrayList(exercises);
            exercises_tableview.setItems(exerciseList);

            exerciseRepository.close();
        });

        search_exercises_button.setOnAction(event -> {
            String nameFilter = exercises_search_textfield.getText().trim().toLowerCase();
            String muscleFilter = muscles_search_textfield.getText().trim().toLowerCase();

            ExerciseRepository exerciseRepository = new ExerciseRepository(CONNECTION_URL, DB_NAME, EXERCISES_COLLECTION);

            List<Exercise> exercises = exerciseRepository.getAllExercises();

            List<Exercise> filteredExercises = exercises.stream()
                    .filter(exercise ->
                            (nameFilter.isEmpty() || exercise.getName().toLowerCase().contains(nameFilter)) &&
                                    (muscleFilter.isEmpty() || exercise.getMuscleGroups().stream()
                                            .anyMatch(muscle -> muscle.toLowerCase().contains(muscleFilter))))
                    .toList();

            ObservableList<Exercise> exerciseList = FXCollections.observableArrayList(filteredExercises);
            exercises_tableview.setItems(exerciseList);

            exerciseRepository.close();
        });

        add_train_button.setOnAction(event -> {
            newtarin_anchorpane.setStyle("-fx-background-color: white; -fx-border-color: gray; -fx-border-width: 1;");
            newtarin_anchorpane.setVisible(true);
            newtarin_anchorpane.toFront();
            create_train_button.setVisible(false);
        });

        add_newexcercise_button.setOnAction(event -> {
            create_train_button.setVisible(true);

            addExerciseBlock(newtrain_vbox);
        });

        create_train_button.setOnAction(event -> saveTraining());

        //trainings_button.setOnAction(event -> loadTrainingsToTableView(trainings_tableview));

        repsColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReps()));
        brzyckiColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBrzycki()));
        epleyColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEpley()));
        landerColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLander()));
        oconnerColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOconner()));

        // Добавление слушателя для обновления таблицы при изменении значений
        weight_spinner.valueProperty().addListener((obs, oldVal, newVal) -> updateTable());
        reps_spinner.valueProperty().addListener((obs, oldVal, newVal) -> updateTable());

        weight_spinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Если введённое значение содержит не цифры
                weight_spinner.getEditor().setText(oldValue); // Возвращаем предыдущее корректное значение
            }
        });

        reps_spinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Если введённое значение содержит не цифры
                reps_spinner.getEditor().setText(oldValue); // Возвращаем предыдущее корректное значение
            }
        });


        calculators_button.setOnAction(event -> showOnlyPane(calculator_anchorpane));

        add_newarticle_button.setOnAction(event -> {
            saveArticle();
        });


        accept_newnickname_button.setVisible(false);
        cancel_change_nickname_button.setVisible(false);
        settings_button.setOnAction(event -> settings_tabpane.setVisible(true));

        nickname_textfield.setEditable(false);
        nickname_textfield.setText(user.getNickname());
        setUserTrainsAmount();

        change_nickname_button.setOnAction(event -> changeButtons());
        accept_newnickname_button.setOnAction(event -> changeNickname());


        newpass_field.setEditable(false);
        accept_newpass_field.setVisible(false);
        accept_newpass_button.setVisible(false);
        cancel_change_pass_button.setVisible(false);

        chang_pass_button.setOnAction(event -> {
            chang_pass_button.setVisible(false);
            newpass_field.setEditable(true);
            accept_newpass_field.setVisible(true);
            accept_newpass_button.setVisible(true);
            cancel_change_pass_button.setVisible(true);
        });
        accept_newpass_button.setOnAction(event -> changePassword());

        cancel_change_nickname_button.setOnAction(event -> {
            nickname_textfield.setText(user.getNickname());
            nickname_textfield.setEditable(false);
            accept_newnickname_button.setVisible(false);
            cancel_change_nickname_button.setVisible(false);
            change_nickname_button.setVisible(true);
        });

        cancel_change_pass_button.setOnAction(event -> {
            newpass_field.setEditable(false);
            accept_newpass_field.setVisible(false);
            accept_newpass_button.setVisible(false);
            cancel_change_pass_button.setVisible(false);
            chang_pass_button.setVisible(true);
        });

        exit_settings1.setOnAction(event -> settings_tabpane.setVisible(false));

        exit_main_button.setOnAction(event -> {
            String result = confirmActionAlert("Выход", "Действительно хотите покинуть учётную запись?");
            if (result.equals("yes")){
                openAuth(event);
            }
        });
        delete_user_button.setOnAction(event -> deleteUser(event));
    }

    private void openAuth(Event event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/authorization-view.fxml"));
            //Parent root = loader.load();

            // Получаем Stage из события
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(loader.load());
            String css = this.getClass().getResource("/styles/authorization.css").toExternalForm();
            scene.getStylesheets().add(css);
            // Создаем и настраиваем новое окно
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("Authorization"); // Заголовок нового окна
            newStage.show();

            // Закрываем старое окно
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Open win");
    }

    private void deleteUser(Event event){
        String result = confirmActionAlert("Удалить пользователя", "Действительно хотите удалить учётную запись?");
        if (result.equals("yes")){
            String result2 = confirmActionAlert("Подтверждение", "Все данные будут удалены без возможности восстановить. Удалить?");
            if (!result2.equals("yes")){
                return;
            }
        }

        UserRepository repository = new UserRepository();
        TrainingRepository trainingRepository = new TrainingRepository(CONNECTION_URL, DB_NAME, TRAININGS_COLLECTION);

        if (!repository.deleteUser(user)){
            showInfoAlert("Ошибка!", "Не удалось удалить учётную запись");
            return;
        }
        if (trainingRepository.deleteTrainingsByUserId(user.getId())){
            System.out.println("Данные пользователя удалены!");
        }
        else{
            System.out.println("Не удалось удалить данные пользователя " + user.getNickname() + " id: " + user.getId());
        }
        openAuth(event);
    }

    private void changePassword() {

        String newPass = newpass_field.getText();
        String acceptNewPass = accept_newpass_field.getText();

        if (newPass.length() < 4 || newPass.length() > 50){
            showInfoAlert("Неверно!", "Пароль должен быть от 4 до 50 символов");
            return;
        }
        if (!newPass.equals(acceptNewPass)){
            showInfoAlert("Неверно!", "Пароль не подтверждён");
            return;
        }
        UserRepository userRepository = new UserRepository();
        User newUser = new User(user.getId(), user.getNickname(), newPass);

        if(userRepository.updateUser(newUser)){
            showInfoAlert("Успешно!", "Пароль обновлён!");
            user.setPassword(newPass);

            newpass_field.setEditable(false);
            accept_newpass_field.setVisible(false);
            accept_newpass_button.setVisible(false);
            cancel_change_pass_button.setVisible(false);
            chang_pass_button.setVisible(true);
        }
        else {
            showInfoAlert("Ошибка!", "Не удалось обновить пароль!");
        }
    }

    private void changeNickname() {

        String newNickname = nickname_textfield.getText();

        if (newNickname.equals(user.getNickname())){
            showInfoAlert("Неверно!", "Новый никнейм не должен соответствовать старому!");
            return;
        }
        if (newNickname.length() < 4 || newNickname.length() > 50) {
            showInfoAlert("Неверно!", "Никнейм должен быть от 4 до 50 символов");
            return;
        }
        UserRepository userRepository = new UserRepository();
        User newUser = new User(user.getId(), newNickname, user.getPassword());
        if(userRepository.updateUser(newUser)){
            showInfoAlert("Успешно!", "Имя обновлено!");
            user.setNickname(newNickname);
            nickname_textfield.setText(newNickname);
            user_label.setText(newNickname);
            nickname_textfield.setEditable(false);

            accept_newnickname_button.setVisible(false);
            cancel_change_nickname_button.setVisible(false);
            change_nickname_button.setVisible(true);
        }
        else {
            showInfoAlert("Ошибка!", "Не удалось обновить имя!");
        }

    }

    private void changeButtons() {

        nickname_textfield.setEditable(true);

        accept_newnickname_button.setVisible(true);
        cancel_change_nickname_button.setVisible(true);
        change_nickname_button.setVisible(false);
    }

    private void setUserTrainsAmount() {
        TrainingRepository trainingRepository = new TrainingRepository(CONNECTION_URL, DB_NAME, TRAININGS_COLLECTION);
        int amount = trainingRepository.getTrainingsByUserId(user.getId()).size();
        if (amount > 0){
            trains_amount_label.setText("Количество тренировок: " + amount);
        }
        else {
            trains_amount_label.setText("Ещё нет тренировок");
        }
    }

    private void exitNewTrain() {
        if (!newtrain_vbox.getChildren().isEmpty() || !train_description_textarea.getText().isEmpty()){
            String result = confirmActionAlert("Выйти?", "Вы действительно хотите завершить создание тренировки? Тренировка не будет сохранена!");

            if (result.equals("yes")) {
                newtarin_anchorpane.setVisible(false);
                newtrain_vbox.getChildren().clear();
                train_description_textarea.setText("");
                train_date_datepicker.setValue(null);
                return;
            }
        }
        newtarin_anchorpane.setVisible(false);
        newtrain_vbox.getChildren().clear();
        train_description_textarea.setText("");
        train_date_datepicker.setValue(null);

    }

    private void exitCreateArticle() {
        String title = edit_article_title_field.getText();
        String articleHTML = edit_article_content_htmleditor.getHtmlText();
        if (!articleHTML.matches("(?i).*<body[^>]*>\\s*</body>.*") || !title.isEmpty()){
            //System.out.println("---------|" + articleHTML + "|---------");
            String result = confirmActionAlert("Выйти?", "Вы действительно хотите выйти? Статья не будет сохранена!");
            if (result.equals("yes")){
                create_article_anchorpane.setVisible(false);
                return;
            }
        }
        create_article_anchorpane.setVisible(false);
    }

    private void deleteArticle() {
        String result = confirmActionAlert("Удалить?", "Хотите удалить данную статью?");
        if (result.equals("yes")){
            ArticleRepository repository = new ArticleRepository(CONNECTION_URL, DB_NAME, ARTICLES_COLLECTION);
            if(repository.deleteArticleById(selectedArticle.getId())){
                loadArticles(repository);
                article_view_pane.setVisible(false);
                showInfoAlert("Успех!", "Статья успешно удалена!");
            }else {
                showInfoAlert("Ошибка!", "Не удалось удалить статью!");
            }
        }

    }

    private void saveEditedArticle() {
        String title = edit_article_title_field.getText();
        if (title.isEmpty()){
            showInfoAlert("Неверные данные", "Название статьи не должно быть пустым");
            return;
        }
        String articleHTML = edit_article_content_htmleditor.getHtmlText();
        if (articleHTML.isEmpty()){
            showInfoAlert("Неверные данные", "Статья не может быть пустой");
            return;
        }

        String result = confirmActionAlert("Сохранить изменения?", "Статья будет изменена!");

        if (result.equals("yes")){
            ArticleRepository repository = new ArticleRepository(CONNECTION_URL, DB_NAME, ARTICLES_COLLECTION);
            Article article = new Article(title, articleHTML, LocalDate.now());
            article.setId(selectedArticle.getId());
            repository.updateArticle(article);
            loadArticles(repository);
            changesSaved = true;
        }


    }

    private void saveArticle() {
        ArticleRepository repository = new ArticleRepository(CONNECTION_URL, DB_NAME, ARTICLES_COLLECTION);
        String title = article_title_textfield.getText();
        if (title.isEmpty()){
            showInfoAlert("Неверные данные", "Название статьи не должно быть пустым");
            return;
        }
        String articleHTML = article_htmleditor.getHtmlText();
        if (articleHTML.isEmpty()){
            showInfoAlert("Неверные данные", "Статья не может быть пустой");
            return;
        }

        Article article = new Article(title, articleHTML, LocalDate.now());
        repository.saveArticle(article);

        article_title_textfield.setText("");
        article_htmleditor.setHtmlText("");

        loadArticles(repository);
    }

    private void addExerciseBlock(VBox mainVBox) {
        ExerciseRepository exerciseRepository = new ExerciseRepository(CONNECTION_URL, DB_NAME, EXERCISES_COLLECTION);

        VBox exerciseBlock = new VBox(5);
        exerciseBlock.setStyle("-fx-border-color: gray; -fx-padding: 10; -fx-border-width: 1;");

        // ComboBox для выбора упражнения
        List<Exercise> exercises = exerciseRepository.getAllExercises();
        ComboBox<String> exerciseComboBox = new ComboBox<>();
        for (Exercise exercise : exercises) {
            exerciseComboBox.getItems().add(exercise.getName());
        }
        exerciseComboBox.setPromptText("Выберите упражнение");

        // Поле для ввода количества подходов
        TextField setsField = new TextField();
        setsField.setPromptText("Количество подходов");

        // Контейнер для полей повторений
        VBox repsContainer = new VBox(5);

        // Обработчик для добавления полей повторений
        setsField.setOnAction(event -> {
            repsContainer.getChildren().clear(); // Очищаем старые поля
            try {
                int sets = Integer.parseInt(setsField.getText());
                for (int i = 1; i <= sets; i++) {
                    TextField repsField = new TextField();
                    repsField.setPromptText("Повторения для подхода " + i);
                    repsContainer.getChildren().add(repsField);
                }
            } catch (NumberFormatException e) {
                // Обрабатываем неверный ввод
                Alert alert = new Alert(Alert.AlertType.ERROR, "Введите целое число для подходов!");
                alert.showAndWait();
            }
        });

        // Добавляем все элементы в блок упражнения
        exerciseBlock.getChildren().addAll(exerciseComboBox, setsField, repsContainer);

        // Добавляем блок в основной VBox
        mainVBox.getChildren().add(exerciseBlock);
        exerciseRepository.close();
    }

    private void saveTraining() {
        ExerciseRepository exerciseRepository = new ExerciseRepository(CONNECTION_URL, DB_NAME, EXERCISES_COLLECTION);
        String name = train_description_textarea.getText().trim();
        LocalDate date = train_date_datepicker.getValue();
        if (name.isEmpty() || date == null) {
            showErrorAlert("Ошибка", "Введите название тренировки и выберите дату.");
            return;
        }

        Map<String, List<Integer>> exerciseMap = new HashMap<>();

        for (Node node : newtrain_vbox.getChildren()) {
            if (node instanceof VBox exerciseBlock) {
                ComboBox<String> exerciseComboBox = (ComboBox<String>) exerciseBlock.getChildren().get(0);
                TextField setsField = (TextField) exerciseBlock.getChildren().get(1);
                VBox repsContainer = (VBox) exerciseBlock.getChildren().get(2);

                String exerciseName = exerciseComboBox.getValue();


                if (exerciseName == null || exerciseName.isEmpty()) {
                    continue;
                }

                try {
                    List<Integer> reps = new ArrayList<>();
                    for (Node repNode : repsContainer.getChildren()) {
                        if (repNode instanceof TextField repsField) {
                            reps.add(Integer.parseInt(repsField.getText().trim()));
                        }
                    }
                    String exerciseId = exerciseRepository.getExerciseIdByName(exerciseName);
                    exerciseMap.put(exerciseId, reps);
                } catch (NumberFormatException e) {
                    showErrorAlert("Ошибка", "Введите корректное число повторений для подходов.");
                    return;
                }
            }
        }

        if (exerciseMap.isEmpty()) {
            showErrorAlert("Ошибка", "Добавьте хотя бы одно упражнение.");
            return;
        }

        TrainingRepository trainingRepository = new TrainingRepository(CONNECTION_URL, DB_NAME, TRAININGS_COLLECTION);
        String userId = String.valueOf(user.getId());
        Training newTraining = new Training(UUID.randomUUID().toString(), userId, date, name, exerciseMap);
        trainingRepository.saveTraining(newTraining);
        trainingRepository.close();

        showInfoAlert("Успех", "Тренировка успешно сохранена!");
        clearTrainingForm();
        loadTrainingsToTableView(trainings_tableview);
    }

    private void clearTrainingForm() {
        train_description_textarea.clear();
        train_date_datepicker.setValue(null);
        newtrain_vbox.getChildren().clear();
    }

    public void loadTrainingsToTableView(TableView<TrainingTableRow> trainingsTableView) {
        // Получаем текущего пользователя
        long userId = user.getId();

        TrainingRepository trainingRepository = new TrainingRepository(CONNECTION_URL, DB_NAME, TRAININGS_COLLECTION);
        ExerciseRepository exerciseRepository = new ExerciseRepository(CONNECTION_URL, DB_NAME, EXERCISES_COLLECTION);

        // Получаем тренировки для текущего пользователя
        List<Training> trainings = trainingRepository.getTrainingsByUserId(userId);

        // Создаем список для хранения строк данных для таблицы
        ObservableList<TrainingTableRow> trainingRows = FXCollections.observableArrayList();

        for (Training training : trainings) {
            // Для каждой тренировки извлекаем данные
            String trainName = training.getName();
            String trainDate = training.getDate().toString();
            Map<String, List<Integer>> exercises = training.getExerciseMap();

            // Получаем мышечные группы
            StringBuilder muscles = new StringBuilder();
            for (Map.Entry<String, List<Integer>> entry : training.getExerciseMap().entrySet()) {
                // Здесь можно работать с id упражнения, если оно связано с конкретной мышечной группой
                // Для примера просто добавим id упражнения как мышечную группу
                List<String> muscleGroups = exerciseRepository.getMuscleGroupsByExerciseId(entry.getKey());

                if (muscles.length() > 0) {
                    muscles.append(", ");
                }
                muscles.append(String.join(", ", muscleGroups)); // предполагаем, что exerciseId это мышечная группа
            }

            // Создаем объект строки таблицы
            trainingRows.add(new TrainingTableRow(trainName, trainDate, muscles.toString()));
        }

        // Наполняем таблицу
        train_name_column.setCellValueFactory(new PropertyValueFactory<>("trainName"));
        train_date_column.setCellValueFactory(new PropertyValueFactory<>("trainDate"));
        train_muscles_column.setCellValueFactory(new PropertyValueFactory<>("muscles"));

        // Устанавливаем данные в таблицу
        trainingsTableView.setItems(trainingRows);
        trainingRepository.close();
        exerciseRepository.close();
    }


    private void filterTrainingsByDate() {
        LocalDate startDate = start_datepicker.getValue() != null ? start_datepicker.getValue() : LocalDate.of(1900, 1, 1);
        LocalDate endDate = end_datepicker.getValue() != null ? end_datepicker.getValue() : LocalDate.now();

        ObservableList<TrainingTableRow> filteredRows = FXCollections.observableArrayList();
        for (TrainingTableRow row : trainings_tableview.getItems()) {
            LocalDate trainDate = LocalDate.parse(row.getTrainDate());
            if ((trainDate.isEqual(startDate) || trainDate.isAfter(startDate)) &&
                    (trainDate.isEqual(endDate) || trainDate.isBefore(endDate))) {
                filteredRows.add(row);
            }
        }
        trainings_tableview.setItems(filteredRows);
    }


    private void filterTrainingsByNameAndMuscle() {
        String nameFilter = search_train_name_textfield.getText().trim().toLowerCase();
        String muscleFilter = search_train_muscle_textfield.getText().trim().toLowerCase();

        ObservableList<TrainingTableRow> filteredRows = FXCollections.observableArrayList();
        for (TrainingTableRow row : trainings_tableview.getItems()) {
            boolean matchesName = nameFilter.isEmpty() || row.getTrainName().toLowerCase().contains(nameFilter);
            boolean matchesMuscle = muscleFilter.isEmpty() || row.getMuscles().toLowerCase().contains(muscleFilter);

            if (matchesName && matchesMuscle) {
                filteredRows.add(row);
            }
        }
        trainings_tableview.setItems(filteredRows);
    }

    private void applyFilters() {
        LocalDate startDate = start_datepicker.getValue() != null ? start_datepicker.getValue() : LocalDate.of(1999, 1, 1);
        LocalDate endDate = end_datepicker.getValue() != null ? end_datepicker.getValue() : LocalDate.now();
        String nameFilter = search_train_name_textfield.getText().trim().toLowerCase();
        String muscleFilter = search_train_muscle_textfield.getText().trim().toLowerCase();

        // Фильтруем оригинальный набор данных
        ObservableList<TrainingTableRow> filteredRows = FXCollections.observableArrayList();
        for (TrainingTableRow row : originalData) {
            LocalDate trainDate = LocalDate.parse(row.getTrainDate());
            boolean matchesDate = (trainDate.isEqual(startDate) || trainDate.isAfter(startDate)) &&
                    (trainDate.isEqual(endDate) || trainDate.isBefore(endDate));
            boolean matchesName = nameFilter.isEmpty() || row.getTrainName().toLowerCase().contains(nameFilter);
            boolean matchesMuscle = muscleFilter.isEmpty() || row.getMuscles().toLowerCase().contains(muscleFilter);

            // Логика "или" для текстовых фильтров
            if (matchesDate && (matchesName && matchesMuscle)) {
                filteredRows.add(row);
            }
        }
        trainings_tableview.setItems(filteredRows);
    }


    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String confirmActionAlert(String header, String Content){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение действия");
        alert.setHeaderText(header);
        alert.setContentText(Content);

        ButtonType yesButton = new ButtonType("Да");
        ButtonType noButton = new ButtonType("Нет");
        ButtonType cancelButton = new ButtonType("Отмена");

        alert.getButtonTypes().setAll(yesButton, noButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == yesButton) {
                return "yes";
            } else if (result.get() == noButton) {
                return "no";
            } else {
                return "cancel";
            }
        }
        return "null";

    }

    private void updateTable() {
        Double weight = weight_spinner.getValue();
        int reps = reps_spinner.getValue();

        try {
            ObservableList<ResultRow> data = calculateResults(weight, reps);
            calc_tableview.setItems(data);
        } catch (NumberFormatException e) {
            // Обработка некорректного ввода
            calc_tableview.setItems(FXCollections.observableArrayList());
        }
    }

    private ObservableList<ResultRow> calculateResults(Double weight, int reps) {
        ObservableList<ResultRow> data = FXCollections.observableArrayList();

        // Расчет одноповторного максимума
        double brzyckiMax = weight / (1.0278 - 0.0278 * reps);
        double epleyMax = weight * (1 + 0.0333 * reps);
        double landerMax = (100 * weight) / (101.3 - 2.67123 * reps);
        double oconnerMax = weight * (1 + 0.025 * reps);

        // Добавление строки с одноповторным максимумом
        data.add(new ResultRow("Одноповторный максимум", format(brzyckiMax), format(epleyMax), format(landerMax), format(oconnerMax)));

        // Проценты и диапазоны повторений
        double[][] ranges = {
                {50, 38, 49}, {55, 30, 37}, {60, 26, 29}, {65, 18, 25},
                {70, 12, 17}, {75, 10, 11}, {80, 8, 9}, {85, 6, 7},
                {90, 4, 5}, {95, 2, 3}
        };

        for (double[] range : ranges) {
            double percent = range[0] / 100;
            String repsRange = (int) range[1] + " - " + (int) range[2];
            data.add(new ResultRow(
                    repsRange,
                    format(brzyckiMax * percent),
                    format(epleyMax * percent),
                    format(landerMax * percent),
                    format(oconnerMax * percent)
            ));
        }

        return data;
    }

    private String format(double value) {
        return String.format("%.2f", value);
    }


    private void
    loadArticles(ArticleRepository repository) {
        List<Article> articles = repository.getAllArticles();
        ObservableList<Article> articleList = FXCollections.observableArrayList(articles);
        articles_tableview.setItems(articleList);
    }

    private void showCreateArticlePane() {
        create_article_anchorpane.setVisible(true);

    }

    private void showArticlePane(Article article) {
        articles_anchorpane.setVisible(false);
        article_view_pane.setVisible(true);
    }


    private void showArticleDetails(Article article) {
        // Устанавливаем данные выбранной статьи
        article_title_label.setText(article.getTitle());
        article_content_webview.getEngine().loadContent(article.getContent());
        article_date_label.setText(article.getDate().toString());

        // Показываем панель просмотра статьи
        article_view_pane.setVisible(true);
    }

    private void closeArticleView() {
        // Скрываем панель просмотра статьи
        article_view_pane.setVisible(false);
    }


    private void showOnlyPane(Pane visiblePane) {
        for (Region pane : panes) {
            pane.setVisible(pane == visiblePane);
        }
    }
}
