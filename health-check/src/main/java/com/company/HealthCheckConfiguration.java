package com.company;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import io.dropwizard.Configuration;
import okhttp3.OkHttpClient;

public class HealthCheckConfiguration extends Configuration {

    private String defaultName = "LogAccessHealthCheck";
    private MongoClient mongoClient;
    private OkHttpClient httpClient;

    public HealthCheckConfiguration() {
        MongoClientOptions options = MongoClientOptions.builder()
                .serverSelectionTimeout(2000)
                .build();
        this.mongoClient = new MongoClient(new ServerAddress("localhost", 27017), options);
        this.httpClient = new OkHttpClient();
    }

    public OkHttpClient getHttpClient() {
        return httpClient;
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
