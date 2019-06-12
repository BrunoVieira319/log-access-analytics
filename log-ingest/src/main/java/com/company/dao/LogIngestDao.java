package com.company.dao;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.List;

public class LogIngestDao implements BaseDao {

    private MongoDatabase db;

    public LogIngestDao(MongoClient mongoClient) {
        this.db = mongoClient.getDatabase("logs");
    }

    @Override
    public void insertMany(List<Document> logs) {
        db.getCollection("logs").insertMany(logs);
    }
}
