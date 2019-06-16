package com.company.dao;

import com.company.util.LogFile;
import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.LoggerConfig;
import com.google.common.flogger.StackSize;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.List;

public class LogIngestDao implements BaseDao {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private MongoDatabase db;

    public LogIngestDao(MongoClient mongoClient) {
        this.db = mongoClient.getDatabase("logs");
        LoggerConfig.of(logger).addHandler(LogFile.getLogFile());
    }

    @Override
    public void insertMany(List<Document> logs) {
        logger.atInfo().log("Persisting data");
        db.getCollection("logs").insertMany(logs);
    }
}
