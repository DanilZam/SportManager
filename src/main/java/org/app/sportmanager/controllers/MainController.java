package org.app.sportmanager.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.app.sportmanager.models.Exercise;
import org.app.sportmanager.repositories.ExerciseRepository;

import java.util.List;

public class MainController {


    private final String CONNECTION_URL = "mongodb://localhost:27017";
    private final String DB_NAME = "sportmanager";
    private final String COLLECTION_NAME = "exercises";


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
    public TableView<?> trainings_tableview;

    @FXML
    public Region user_icon_region;

    @FXML
    public Label user_label;

    @FXML
    public Button create_train_button;


    @FXML
    public void initialize() {

        exercises_tableview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        trainings_anchorpane.setVisible(true);
        newtarin_anchorpane.setVisible(false);
        exercises_anchorpane.setVisible(false);

        name_column.setCellValueFactory(new PropertyValueFactory<>("name"));
        musclegroups_columns.setCellValueFactory(cellData -> {
            // Преобразуем список мышечных групп в строку, разделенную запятыми
            String muscleGroups = String.join(", ", cellData.getValue().getMuscleGroups());
            return new javafx.beans.property.SimpleStringProperty(muscleGroups);
        });

        // Устанавливаем редактирование ячеек (если нужно)
        musclegroups_columns.setCellFactory(TextFieldTableCell.forTableColumn());



        trainings_button.setOnAction(event -> {
            trainings_anchorpane.setVisible(true);
            newtarin_anchorpane.setVisible(false);
            exercises_anchorpane.setVisible(false);
        });

        exercises_button.setOnAction(event -> {
            trainings_anchorpane.setVisible(false);
            newtarin_anchorpane.setVisible(false);
            exercises_anchorpane.setVisible(true);

            ExerciseRepository exerciseRepository = new ExerciseRepository(CONNECTION_URL, DB_NAME, COLLECTION_NAME);

            List<Exercise> exercises = exerciseRepository.getAllExercises();

            ObservableList<Exercise> exerciseList = FXCollections.observableArrayList(exercises);
            exercises_tableview.setItems(exerciseList);

            exerciseRepository.close();
        });

        search_exercises_button.setOnAction(event -> {
            String nameFilter = exercises_search_textfield.getText().trim().toLowerCase();
            String muscleFilter = muscles_search_textfield.getText().trim().toLowerCase();

            ExerciseRepository exerciseRepository = new ExerciseRepository(CONNECTION_URL, DB_NAME, COLLECTION_NAME);

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

            create_train_button.setVisible(false);
        });

        add_newexcercise_button.setOnAction(event -> {
            create_train_button.setVisible(true);

            addExerciseBlock(newtrain_vbox);
        });

    }

    private void addExerciseBlock(VBox mainVBox) {
        VBox exerciseBlock = new VBox(5);
        exerciseBlock.setStyle("-fx-border-color: gray; -fx-padding: 10; -fx-border-width: 1;");

        // ComboBox для выбора упражнения
        ComboBox<String> exerciseComboBox = new ComboBox<>();
        exerciseComboBox.getItems().addAll("Приседания", "Жим лёжа", "Подтягивания");
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
    }

}

