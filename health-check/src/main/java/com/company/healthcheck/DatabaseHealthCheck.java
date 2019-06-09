package com.company.healthcheck;

import com.codahale.metrics.health.HealthCheck;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

public class DatabaseHealthCheck extends HealthCheck {

    private MongoClient mongo;

    @Override
    protected Result check() throws Exception {
        MongoClientOptions options = MongoClientOptions.builder().serverSelectionTimeout(2000).build();
        mongo = new MongoClient(new ServerAddress("localhost", 27017), options);
        try {
            mongo.getDatabase("logs").getCollection("logs").countDocuments();
        } catch (Exception e) {
            return Result.unhealthy("Failed to connect to localhost/127.0.0.1:27017");
        }
        mongo.close();
        return Result.healthy();
    }
}
