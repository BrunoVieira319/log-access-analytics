package com.company.dao;

import com.company.db.EmbeddedMongoDb;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class LogIngestDaoTest {

    EmbeddedMongoDb db;
    MongoClient mongoClient;

    @Before
    public void configureMongoForTests() {
        db = new EmbeddedMongoDb();
        db.start("localhost", 27018);
        mongoClient = db.getMongoClient();
    }

    @After
    public void closeConnection() {
        db.close();
    }

    @Test
    public void shouldPersistFiveLogsInDB() {
        LogIngestDao baseDao = new LogIngestDao(mongoClient);

        List<Document> logs = new ArrayList<>();
        IntStream.range(0, 5).forEach(n -> logs.add(new Document()));

        baseDao.insertMany(logs);

        MongoCollection<Document> collection = mongoClient.getDatabase("logs").getCollection("logs");
        assertEquals(collection.countDocuments(), 5);
    }
}
