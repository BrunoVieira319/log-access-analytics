package com.company;

import com.mongodb.MongoClient;
import io.dropwizard.Configuration;

public class LogIngestConfiguration extends Configuration {

    private String defaultName = "LogIngest";
    private MongoClient mongoClient;

    public LogIngestConfiguration() {
        this.mongoClient = new MongoClient("localhost", 27017);
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }
}
