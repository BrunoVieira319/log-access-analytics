package com.company;

import com.mongodb.MongoClient;
import io.dropwizard.Configuration;

public class MetricsConfiguration extends Configuration {

    private String defaultName = "Metrics";
    private MongoClient mongoClient;

    public MetricsConfiguration() {
        this.mongoClient = new MongoClient("localhost", 27017);
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public String getDefaultName() {
        return defaultName;
    }

}
