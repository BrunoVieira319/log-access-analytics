package com.company;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import io.dropwizard.Configuration;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class HealthCheckConfiguration extends Configuration {

    private String defaultName = "HealthCheck";
    private MongoClient mongoClient;
    private Client httpClient;

    public HealthCheckConfiguration() {
        MongoClientOptions options = MongoClientOptions.builder()
                .serverSelectionTimeout(2000)
                .build();
        this.mongoClient = new MongoClient(new ServerAddress("localhost", 27017), options);
        this.httpClient = ClientBuilder.newClient();
    }

    public Client getHttpClient() {
        return httpClient;
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public String getDefaultName() {
        return defaultName;
    }
}
