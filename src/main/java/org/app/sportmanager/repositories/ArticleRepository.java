package org.app.sportmanager.repositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import org.app.sportmanager.models.Article;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ArticleRepository {

    private final MongoClient mongoClient;
    private final MongoCollection<Document> collection;

    public ArticleRepository(String connectionString, String databaseName, String collectionName) {
        // Подключение к MongoDB
        mongoClient = MongoClients.create(connectionString);
        collection = mongoClient.getDatabase(databaseName).getCollection(collectionName);
    }

    public void saveArticle(Article article) {
        Document doc = new Document("_id", article.getId())
                .append("title", article.getTitle())
                .append("content", article.getContent())
                .append("date", article.getDate().toString());
        collection.insertOne(doc);
    }

    public List<Article> getAllArticles() {
        List<Article> articles = new ArrayList<>();
        for (Document doc : collection.find()) {
            String id = doc.getString("_id");
            String title = doc.getString("title");
            String content = doc.getString("content");
            String date = doc.getString("date");
            articles.add(new Article(id, title, content, LocalDate.parse(date)));
        }
        return articles;
    }

    public void updateArticle(Article article) {
        collection.updateOne(
                Filters.eq("_id", article.getId()),
                Updates.combine(
                        Updates.set("title", article.getTitle()),
                        Updates.set("content", article.getContent()),
                        Updates.set("date", article.getDate().toString())
                )
        );
    }

    public boolean deleteArticleById(String id) {
        try {
            DeleteResult result = collection.deleteOne(Filters.eq("_id", id));
            return result.getDeletedCount() > 0;
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid ID format: " + e.getMessage());
            return false;
        }
    }

}
