package org.app.sportmanager.repositories;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import org.app.sportmanager.models.Exercise;
import org.app.sportmanager.models.Training;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainingRepository {

    private final MongoCollection<Document> collection;
    private final MongoClient mongoClient; // Сохраняем MongoClient

    public TrainingRepository(String connectionUrl, String dbName, String collectionName) {
        this.mongoClient = MongoClients.create(connectionUrl); // Инициализация mongoClient
        MongoDatabase database = mongoClient.getDatabase(dbName);
        this.collection = database.getCollection(collectionName);
    }

    // Получение всех тренировок
    public List<Training> getAllTrainings() {
        List<Training> trainings = new ArrayList<>();

        // Получение данных из MongoDB
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                trainings.add(documentToTraining(doc));
            }
        }
        return trainings;
    }

    public List<Training> getTrainingsByUserId(long userId) {
        List<Training> trainings = new ArrayList<>();

        // Получение данных из MongoDB для текущего пользователя
        try (MongoCursor<Document> cursor = collection.find(Filters.eq("userId", String.valueOf(userId))).iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                trainings.add(documentToTraining(doc));
            }
        }
        return trainings;
    }

    public void saveTraining(Training training) {
        Document document = new Document("_id", training.getId())
                .append("userId", training.getUserId())
                .append("date", training.getDate().toString())
                .append("name", training.getName())
                .append("exerciseMap", training.getExerciseMap());
        collection.insertOne(document);
    }

    // Преобразование Document в Training
    public Training documentToTraining(Document document) {
        Training training = new Training();

        // Получаем _id как строку (UUID), а не как ObjectId
        String id = document.getString("_id");
        training.setId(id); // Устанавливаем строковый id в Training

        training.setUserId(document.getString("userId"));
        //training.setDate(document.getDate("date").toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        training.setDate(LocalDate.parse(document.getString("date")));
        training.setName(document.getString("name"));

        // Обрабатываем карту упражнений
        Map<String, List<Integer>> exerciseMap = new HashMap<>();
        document.get("exerciseMap", Document.class).forEach((exercise, reps) -> {
            exerciseMap.put(exercise, (List<Integer>) reps);
        });
        training.setExerciseMap(exerciseMap);

        return training;
    }

    // Преобразование Training в Document
    private Document trainingToDocument(Training training) {
        Document doc = new Document()
                .append("userid", training.getUserId())
                .append("date", training.getDate())
                .append("name", training.getName());

        List<Document> exercises = new ArrayList<>();
        for (Map.Entry<String, List<Integer>> entry : training.getExerciseMap().entrySet()) {
            Document exerciseDoc = new Document()
                    .append("id", entry.getKey())
                    .append("sets", entry.getValue());
            exercises.add(exerciseDoc);
        }

        doc.append("exercises", exercises);

        if (training.getId() != null) {
            doc.append("_id", new ObjectId(training.getId()));
        }

        return doc;
    }

    public boolean deleteTrainingsByUserId(long userId) {
        try {
            // Удаляем все записи с указанным userId
            DeleteResult result = collection.deleteMany(Filters.eq("userId", String.valueOf(userId)));
            long deletedCount = result.getDeletedCount();

            if (deletedCount > 0) {
                System.out.println("Успешно удалено тренировок: " + deletedCount);
                return true; // Подтверждаем успех
            } else {
                System.out.println("Не найдено тренировок для удаления с userId " + userId);
                return false; // Ничего не удалено
            }
        } catch (Exception e) {
            System.err.println("Ошибка при удалении тренировок пользователя с userId " + userId);
            e.printStackTrace();
            return false; // В случае ошибки
        }
    }


    public void close() {
        mongoClient.close(); // Закрытие mongoClient
    }
}
