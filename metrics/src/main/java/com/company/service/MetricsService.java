package com.company.service;

import com.company.dto.LogDto;
import com.company.dto.RegionDto;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MetricsService {

    MongoClient mongoClient;
    MongoDatabase db;

    public MetricsService() {
        this.mongoClient = new MongoClient("localhost", 27017);
        this.db = mongoClient.getDatabase("logs");
    }

    public List<LogDto> findUrlsMostVisited(int limit) {
        AggregateIterable<Document> queryResult = db.getCollection("logs").aggregate(
                Arrays.asList(
                        Aggregates.sortByCount("$url")
                )
        );

        return getTop(logsToList(queryResult), limit);
    }

    public List<LogDto> findUrlsLessVisited(int limit) {
        AggregateIterable<Document> queryResult = db.getCollection("logs").aggregate(
                Arrays.asList(
                        Aggregates.sortByCount("$url"),
                        Aggregates.sort(new Document("count", 1))
                )
        );

        return getTop(logsToList(queryResult), limit);
    }

    public List<RegionDto> findUrlsMostVisitedPerRegion(int limit) throws ArrayIndexOutOfBoundsException {
        List<LogDto> allLogs = getLogsGroupedByRegionAndUrl();

        List<LogDto> urlsRegion1 = groupByRegion(allLogs, 1);
        List<LogDto> urlsRegion2 = groupByRegion(allLogs, 2);
        List<LogDto> urlsRegion3 = groupByRegion(allLogs, 3);

        RegionDto region1 = createDto(1, getTop(urlsRegion1, limit));
        RegionDto region2 = createDto(2, getTop(urlsRegion2, limit));
        RegionDto region3 = createDto(3, getTop(urlsRegion3, limit));

        return Arrays.asList(region1, region2, region3);
    }

    private List<LogDto> getLogsGroupedByRegionAndUrl() {
        AggregateIterable queryResult = db.getCollection("logs").aggregate(
                Arrays.asList(
                        new Document("$group",
                                new Document("count", new Document("$sum", 1))
                                        .append("_id",
                                                new Document("region", "$region")
                                                        .append("url", "$url"))
                        )
                )
        );

        List<LogDto> logs = new ArrayList<>();

        queryResult.forEach((Consumer<? super Document>) document -> {
            LogDto log = new LogDto();
            log.setRegion(document.get("_id", Document.class).getInteger("region"));
            log.setUrl(document.get("_id", Document.class).getString("url"));
            log.setCount(document.getInteger("count"));

            logs.add(log);
        });

        return logs;
    }

    private List<LogDto> groupByRegion(List<LogDto> logs, int region) {
        return logs.stream()
                .filter(l -> l.getRegion() == region)
                .sorted(Comparator.comparingInt(LogDto::getCount).reversed())
                .collect(Collectors.toList());
    }

    private List<LogDto> getTop(List<LogDto> list, int limit) {
        int target = list.get(0).getCount();
        int occurrences = (int) list.stream().filter(l -> l.getCount() == target).count();

        if (occurrences > limit) {
            return list.subList(0, occurrences);
        }
        if (list.size() > limit) {
            return list.subList(0, limit);
        }
        return list;
    }

    private RegionDto createDto(int region, List<LogDto> url) {
        RegionDto regionDto = new RegionDto();
        regionDto.setRegion(region);
        regionDto.setTop3url(url);

        return regionDto;
    }

    private List<LogDto> logsToList(AggregateIterable<Document> iterable) {
        List<LogDto> logs = new ArrayList<>();
        iterable.forEach((Consumer<? super Document>) document -> {
            LogDto logDto = new LogDto();
            logDto.setUrl(document.getString("_id"));
            logDto.setCount(document.getInteger("count"));
            logs.add(logDto);
        });

        return logs;
    }


}


