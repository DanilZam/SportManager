package org.app.sportmanager.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.app.sportmanager.UserSession;
import org.app.sportmanager.fx_nodes.TrainingTableRow;
import org.app.sportmanager.models.Exercise;
import org.app.sportmanager.models.Training;
import org.app.sportmanager.repositories.ExerciseRepository;
import org.app.sportmanager.repositories.TrainingRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class MainController {


    private final String CONNECTION_URL = "mongodb://localhost:27017";
    private final String DB_NAME = "sportmanager";
    private final String EXERCISES_COLLECTION = "exercises";
    private final String TRAININGS_COLLECTION = "trainings";

    private ObservableList<TrainingTableRow> originalData = FXCollections.observableArrayList();



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
    public Region user_icon_region;

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
    public void initialize() {

        loadTrainingsToTableView(trainings_tableview);
        originalData.addAll(trainings_tableview.getItems());

        exercises_tableview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        start_datepicker.setOnAction(event -> applyFilters());
        end_datepicker.setOnAction(event -> applyFilters());
        search_train_name_textfield.setOnKeyReleased(event -> applyFilters());
        search_train_muscle_textfield.setOnKeyReleased(event -> applyFilters());


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
            loadTrainingsToTableView(trainings_tableview);
        });

        exercises_button.setOnAction(event -> {
            trainings_anchorpane.setVisible(false);
            newtarin_anchorpane.setVisible(false);
            exercises_anchorpane.setVisible(true);

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

            create_train_button.setVisible(false);
        });

        add_newexcercise_button.setOnAction(event -> {
            create_train_button.setVisible(true);

            addExerciseBlock(newtrain_vbox);
        });

        create_train_button.setOnAction(event -> saveTraining());

        //trainings_button.setOnAction(event -> loadTrainingsToTableView(trainings_tableview));

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
        String userId = String.valueOf(UserSession.getInstance().getCurrentUser().getId());
        Training newTraining = new Training(UUID.randomUUID().toString(), userId, date, name, exerciseMap);
        trainingRepository.saveTraining(newTraining);
        trainingRepository.close();

        showInfoAlert("Успех", "Тренировка успешно сохранена!");
        clearTrainingForm();
    }

    private void clearTrainingForm() {
        train_description_textarea.clear();
        train_date_datepicker.setValue(null);
        newtrain_vbox.getChildren().clear();
    }

    public void loadTrainingsToTableView(TableView<TrainingTableRow> trainingsTableView) {
        // Получаем текущего пользователя
        long userId = UserSession.getInstance().getCurrentUser().getId();

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
}


