package com.company.service;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.List;

public class LogIngestService {

    MongoClient mongoClient;
    MongoDatabase db;

    public LogIngestService() {
        this.mongoClient = new MongoClient("localhost", 27017);
        this.db = mongoClient.getDatabase("logs");
    }

    public void insertLogs(List<Document> logs) {
        db.getCollection("logs").insertMany(logs);
    }
}
