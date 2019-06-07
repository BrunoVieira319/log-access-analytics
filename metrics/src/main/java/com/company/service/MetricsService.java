package com.company.service;

import com.company.dto.UrlsTop3PerRegion;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class MetricsService {

    MongoClient mongoClient;
    MongoDatabase db;

    public MetricsService() {
        this.mongoClient = new MongoClient("localhost", 27017);
        this.db = mongoClient.getDatabase("logs");
    }

    public AggregateIterable<Document> findUrlsMostAcessed(int limit) {
        return db.getCollection("logs").aggregate(
                Arrays.asList(
                        Aggregates.sortByCount("$endpoint"),
                        Aggregates.limit(limit)
                )
        );
    }

    public AggregateIterable<Document> findUrlsMostAcessedPerRegion() {
        return getLogsGroupedByRegion();
    }

    private AggregateIterable<Document> getLogsGroupedByRegion() {
        return db.getCollection("logs").aggregate(
                Arrays.asList(
                        new Document("$group",
                                new Document("counter", new Document("$sum", 1))
                                        .append("_id",
                                                new Document("region", "$region")
                                                .append("url", "$endpoint"))
                        )
                )
        );
    }

    public List<UrlsTop3PerRegion> build(){
        List<UrlsTop3PerRegion> region1 = new ArrayList<>();
        List<UrlsTop3PerRegion> region2 = new ArrayList<>();
        List<UrlsTop3PerRegion> region3 = new ArrayList<>();

        getLogsGroupedByRegion().forEach((Consumer<? super Document>) document -> {
            var log = new UrlsTop3PerRegion();
            log.setUrl(document.get("_id", Document.class).getString("url"));
            log.setCounter(document.getInteger("counter"));
        });

        return null;
    }
}


