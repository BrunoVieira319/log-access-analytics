package com.company.dao;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.List;

public class LogIngestDao implements BaseDao {

    MongoClient mongoClient;
    MongoDatabase db;

    public LogIngestDao(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
        this.db = mongoClient.getDatabase("logs");
    }

    @Override
    public void insertMany(List<Document> logs) {
        try {
            db.getCollection("logs").insertMany(logs);
        } catch (Exception e) {
            System.out.println("Falha ao salvar documentos");
        }
    }
}
