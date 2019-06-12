package com.company.dao;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDate;
import java.util.Arrays;

import static com.mongodb.client.model.Filters.*;

public class MetricsDao implements BaseDao {

    private MongoDatabase db;

    public MetricsDao(MongoClient mongoClient) {
        this.db = mongoClient.getDatabase("logs");
    }

    private Bson getProjectionFields() {
        return Projections.fields(
                Projections.excludeId(),
                Projections.include("count"),
                Projections.computed("url", "$_id")
        );
    }

    @Override
    public Iterable<Document> getLogsGroupedByUrl() {
        Iterable<Document> queryResult = db.getCollection("logs").aggregate(
                Arrays.asList(
                        Aggregates.sortByCount("$url"),
                        Aggregates.project(getProjectionFields())
                )
        );
        return queryResult;
    }

    @Override
    public Iterable<Document> getLogsGroupedByRegionAndUrl() {
        Document group = new Document("region", "$region")
                .append("url", "$url");

        Iterable<Document> queryResult = db.getCollection("logs").aggregate(
                Arrays.asList(
                        Aggregates.group(group, Accumulators.sum("count", 1)),
                        Aggregates.sort(new Document("count", -1)),
                        Aggregates.project(Projections.fields(
                                Projections.include("count"),
                                Projections.computed("url", "$_id.url"),
                                Projections.computed("region", "$_id.region")
                        ))
                )
        );
        return queryResult;
    }

    @Override
    public Iterable<Document> getLogsGroupedByUrlsAccessedBetween(LocalDate initialDate, LocalDate finalDate) {
        Iterable<Document> queryResult = db.getCollection("logs").aggregate(
                Arrays.asList(
                        Aggregates.match(and(gte("timestamp", initialDate), lte("timestamp", finalDate))),
                        Aggregates.sortByCount("$url"),
                        Aggregates.project(getProjectionFields())
                )
        );
        return queryResult;
    }

    @Override
    public Iterable<Document> getLogsGroupedByMinute() {
        Document group = new Document("hour", new Document("$hour", "$timestamp"))
                .append("minute", new Document("$minute", "$timestamp"));

        Iterable<Document> queryResult = db.getCollection("logs").aggregate(
                Arrays.asList(
                        Aggregates.group(group, Accumulators.sum("count", 1)),
                        Aggregates.sort(new Document("count", -1))
                )
        );
        return queryResult;
    }
}
