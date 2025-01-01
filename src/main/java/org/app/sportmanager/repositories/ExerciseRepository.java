package org.app.sportmanager.repositories;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.app.sportmanager.models.Exercise;
import org.bson.Document;
import com.mongodb.ConnectionString;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class ExerciseRepository {

    private final MongoClient mongoClient;
    private final MongoCollection<Document> collection;

    public ExerciseRepository(String connectionString, String databaseName, String collectionName) {
        // Подключение к MongoDB

        mongoClient = MongoClients.create(connectionString);
        collection = mongoClient.getDatabase(databaseName).getCollection(collectionName);
    }

    // Получение всех упражнений
    public List<Exercise> getAllExercises() {
        List<Exercise> exercises = new ArrayList<>();

        // Получение данных из MongoDB
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                exercises.add(documentToExercise(doc));
            }
        }

        return exercises;
    }

    // Преобразование Document в Exercise
    private Exercise documentToExercise(Document doc) {
        String id = doc.get("_id").toString();
        String name = doc.getString("name");
        List<String> muscles = (List<String>) doc.get("muscles");
        String description = doc.getString("description");

        return new Exercise(id, name, muscles, description);
    }

    // Закрытие подключения
    public void close() {
        mongoClient.close();
    }


}