package com.company.healthcheck;

import com.codahale.metrics.health.HealthCheck;
import com.mongodb.MongoClient;
import org.bson.Document;

public class DatabaseHealthCheck extends HealthCheck {

    private MongoClient mongoClient;

    public DatabaseHealthCheck(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public Result check() {
        try {
            mongoClient.getDatabase("logs").runCommand(new Document("ping", 1));
        } catch (Exception e) {
            return Result.unhealthy("Failed to connect to localhost/127.0.0.1:27017");
        }
        mongoClient.close();
        return Result.healthy();
    }
}
