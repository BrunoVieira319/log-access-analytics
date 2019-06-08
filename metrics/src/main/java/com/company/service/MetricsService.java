package com.company.service;

import com.company.dto.DateQueryDto;
import com.company.dto.LogDto;
import com.company.dto.RegionDto;
import com.company.util.DtoCreator;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.*;

public class MetricsService {

    private MongoClient mongoClient;
    private MongoDatabase db;

    public MetricsService() {
        this.mongoClient = new MongoClient("localhost", 27017);
        this.db = mongoClient.getDatabase("logs");
    }

    public List<LogDto> findMostAccessedUrls(int limit) {
        AggregateIterable<Document> queryResult = db.getCollection("logs").aggregate(
                Arrays.asList(
                        Aggregates.sortByCount("$url"),
                        Aggregates.project(getProjectionFields())
                )
        );

        return getTop(logsToList(queryResult), limit);
    }

    public List<LogDto> findLessAccessedUrls(int limit) {
        AggregateIterable<Document> queryResult = db.getCollection("logs").aggregate(
                Arrays.asList(
                        Aggregates.sortByCount("$url"),
                        Aggregates.sort(new Document("count", 1)),
                        Aggregates.project(getProjectionFields())
                )
        );

        return getTop(logsToList(queryResult), limit);
    }

    public List<RegionDto> findMostAccessedUrlsPerRegion(int limit) {
        List<LogDto> allLogs = getLogsGroupedByRegionAndUrl();

        List<LogDto> urlsRegion1 = groupByRegion(allLogs, 1);
        List<LogDto> urlsRegion2 = groupByRegion(allLogs, 2);
        List<LogDto> urlsRegion3 = groupByRegion(allLogs, 3);

        RegionDto region1 = DtoCreator.regionDto(1, getTop(urlsRegion1, limit));
        RegionDto region2 = DtoCreator.regionDto(2, getTop(urlsRegion2, limit));
        RegionDto region3 = DtoCreator.regionDto(3, getTop(urlsRegion3, limit));

        return Arrays.asList(region1, region2, region3);
    }

    public List<DateQueryDto> findMostAccessedUrlsPerDates(String day, String weekYear, String year, int limit) {
        List<LogDto> topOfTheDay = getTop(findAccessedUrlsOnDay(day), limit);
        List<LogDto> topOfTheWeek = getTop(findAccessedUrlsInWeek(weekYear), limit);
        List<LogDto> topOfTheYear = getTop(findAccessedUrlsInYear(year), limit);

        DateQueryDto dayDto = DtoCreator.dateQueryDto("Day: " + day, topOfTheDay);
        DateQueryDto weekDto = DtoCreator.dateQueryDto("Week: " + weekYear, topOfTheWeek);
        DateQueryDto yearDto = DtoCreator.dateQueryDto("Year: " + year, topOfTheYear);

        return Arrays.asList(dayDto, weekDto, yearDto);
    }

    public Document findMostAccessedMinute() {
        Document group = new Document("hour", new Document("$hour", "$timestamp"))
                .append("minute", new Document("$minute", "$timestamp"));

        Iterable<Document> queryResult = db.getCollection("logs").aggregate(
                Arrays.asList(
                        Aggregates.group(group, Accumulators.sum("count", 1)),
                        Aggregates.sort(new Document("count", -1)),
                        Aggregates.limit(1)
                )
        );

        return queryResult.iterator().next();
    }

    private List<LogDto> findAccessedUrlsOnDay(String day) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate localDate = LocalDate.parse(day, formatter);

            return findAccessedUrlsBetween(localDate, localDate.plusDays(1));
        } catch (Exception e) {
            System.out.println("Parâmetro day inválido");
            return null;
        }
    }

    private List<LogDto> findAccessedUrlsInWeek(String weekYear) {
        try {
            String[] separatedWeekYear = weekYear.split("-");
            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            LocalDate firstWeekDay = LocalDate.now()
                    .withYear(Integer.valueOf(separatedWeekYear[1]))
                    .with(weekFields.weekOfYear(), Integer.valueOf(separatedWeekYear[0]))
                    .with(weekFields.dayOfWeek(), 1);

            LocalDate lastWeekDay = firstWeekDay.plusWeeks(1);

            return findAccessedUrlsBetween(firstWeekDay, lastWeekDay);
        } catch (Exception e) {
            System.out.println("Parâmetro week inválido");
            return null;
        }
    }

    private List<LogDto> findAccessedUrlsInYear(String year) {
        try {
            Year y = Year.of(Integer.valueOf(year));
            LocalDate firstDayOfTheYear = y.atDay(1);
            LocalDate lastDayOfTheYear = firstDayOfTheYear.plusYears(1);

            return findAccessedUrlsBetween(firstDayOfTheYear, lastDayOfTheYear);
        } catch (Exception e) {
            System.out.println("Parâmetro year inválido");
            return null;
        }
    }

    private List<LogDto> findAccessedUrlsBetween(LocalDate initialDate, LocalDate finalDate) {
        Iterable<Document> queryResult = db.getCollection("logs").aggregate(
                Arrays.asList(
                        Aggregates.match(and(gte("timestamp", initialDate), lte("timestamp", finalDate))),
                        Aggregates.sortByCount("$url"),
                        Aggregates.project(getProjectionFields())
                )
        );

        return logsToList(queryResult);
    }

    private List<LogDto> getLogsGroupedByRegionAndUrl() {
        Document group = new Document("region", "$region")
                .append("url", "$url");

        Iterable<Document> queryResult = db.getCollection("logs").aggregate(
                Arrays.asList(
                        Aggregates.group(group, Accumulators.sum("count", 1)),
                        Aggregates.project(Projections.fields(
                                Projections.include("count"),
                                Projections.computed("url", "$_id.url"),
                                Projections.computed("region", "$_id.region")
                        ))
                )
        );
        return logsToList(queryResult);
    }

    private List<LogDto> groupByRegion(List<LogDto> logs, int region) {
        return logs.stream()
                .filter(l -> l.getRegion() == region)
                .sorted(Comparator.comparingInt(LogDto::getCount).reversed())
                .collect(Collectors.toList());
    }

    private List<LogDto> getTop(List<LogDto> list, int limit) {
        try {
            int target = list.get(0).getCount();
            int occurrences = (int) list.stream().filter(l -> l.getCount() == target).count();

            if (occurrences > limit) {
                return list.subList(0, occurrences);
            }
            if (list.size() > limit) {
                return list.subList(0, limit);
            }
            return list;
        } catch (Exception e) {
            System.out.println("Lista vazia, pois nenhum resultado foi encontrado");
            return list;
        }
    }

    private Bson getProjectionFields() {
        return Projections.fields(
                Projections.excludeId(),
                Projections.include("count"),
                Projections.computed("url", "$_id")
        );
    }

    private List<LogDto> logsToList(Iterable<Document> iterable) {
        List<LogDto> logs = new ArrayList<>();
        iterable.forEach(document -> {
            LogDto logDto = DtoCreator.logDto(
                    document.getInteger("region"),
                    document.getString("url"),
                    document.getInteger("count"));
            logs.add(logDto);
        });

        return logs;
    }

}