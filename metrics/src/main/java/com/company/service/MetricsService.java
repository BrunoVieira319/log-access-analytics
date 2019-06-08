package com.company.service;

import com.company.dto.DateQueryDto;
import com.company.dto.LogDto;
import com.company.dto.RegionDto;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Projections;
import org.bson.Document;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
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

    public List<LogDto> findUrlsMostVisited(int limit) {
        AggregateIterable<Document> queryResult = db.getCollection("logs").aggregate(
                Arrays.asList(
                        Aggregates.sortByCount("$url"),
                        Aggregates.project(Projections.fields(
                                Projections.excludeId(),
                                Projections.include("count"),
                                Projections.computed("url", "$_id")
                        ))
                )
        );

        return getTop(logsToList(queryResult), limit);
    }

    public List<LogDto> findUrlsLessVisited(int limit) {
        AggregateIterable<Document> queryResult = db.getCollection("logs").aggregate(
                Arrays.asList(
                        Aggregates.sortByCount("$url"),
                        Aggregates.sort(new Document("count", 1)),
                        Aggregates.project(Projections.fields(
                                Projections.excludeId(),
                                Projections.include("count"),
                                Projections.computed("url", "$_id")
                        ))
                )
        );

        return getTop(logsToList(queryResult), limit);
    }

    public List<RegionDto> findUrlsMostVisitedPerRegion(int limit) {
        List<LogDto> allLogs = getLogsGroupedByRegionAndUrl();

        List<LogDto> urlsRegion1 = groupByRegion(allLogs, 1);
        List<LogDto> urlsRegion2 = groupByRegion(allLogs, 2);
        List<LogDto> urlsRegion3 = groupByRegion(allLogs, 3);

        RegionDto region1 = createRegionDto(1, getTop(urlsRegion1, limit));
        RegionDto region2 = createRegionDto(2, getTop(urlsRegion2, limit));
        RegionDto region3 = createRegionDto(3, getTop(urlsRegion3, limit));

        return Arrays.asList(region1, region2, region3);
    }

    public List<DateQueryDto> findUrlsMostVisitedPerDates(String day, String weekYear, String year, int limit) {
        List<LogDto> topOfTheDay = getTop(findUrlsVisitedOnDay(day), limit);
        List<LogDto> topOfTheWeek = getTop(findUrlsVisitedInWeek(weekYear), limit);
        List<LogDto> topOfTheYear = getTop(findUrlsVisitedInYear(year), limit);

        DateQueryDto dayDto = createDateQueryDto("Day: " + day, topOfTheDay);
        DateQueryDto weekDto = createDateQueryDto("Week: " + weekYear, topOfTheWeek);
        DateQueryDto yearDto =createDateQueryDto("Year: " + year, topOfTheYear);

        return Arrays.asList(dayDto, weekDto, yearDto);
    }

    private List<LogDto> findUrlsVisitedOnDay(String day) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(day, formatter);

        return findUrlsVisitedBetween(localDate, localDate.plusDays(1));
    }

    private List<LogDto> findUrlsVisitedInWeek(String weekYear) {
        String[] weekYearFormat = weekYear.split("-");
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        LocalDate firstWeekDay = LocalDate.now()
                .withYear(Integer.valueOf(weekYearFormat[1]))
                .with(weekFields.weekOfYear(), Integer.valueOf(weekYearFormat[0]))
                .with(weekFields.dayOfWeek(), 1);

        LocalDate lastWeekDay = firstWeekDay.plusWeeks(1);

        return findUrlsVisitedBetween(firstWeekDay, lastWeekDay);
    }

    private List<LogDto> findUrlsVisitedInYear(String year) {
        Year y = Year.of(Integer.valueOf(year));
        LocalDate firstDayOfTheYear = y.atDay(1);
        LocalDate lastDayOfTheYear = firstDayOfTheYear.plusYears(1);

        return findUrlsVisitedBetween(firstDayOfTheYear, lastDayOfTheYear);
    }

    private List<LogDto> findUrlsVisitedBetween(LocalDate start, LocalDate end) {
        Iterable<Document> queryResult = db.getCollection("logs").aggregate(
                Arrays.asList(
                        Aggregates.match(and(gte("timestamp", start), lte("timestamp", end))),
                        Aggregates.sortByCount("$url"),
                        Aggregates.project(Projections.fields(
                                Projections.excludeId(),
                                Projections.include("count"),
                                Projections.computed("url", "$_id")
                        ))
                )
        );

        return logsToList(queryResult);
    }

    private List<LogDto> getLogsGroupedByRegionAndUrl() {
        Iterable<Document> queryResult = db.getCollection("logs").aggregate(
                Arrays.asList(
                        new Document("$group",
                                new Document("count", new Document("$sum", 1))
                                        .append("_id",
                                                new Document("region", "$region")
                                                        .append("url", "$url"))
                        ),
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
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Lista vazia, pois nenhum resultado foi encontrado");
            return list;
        }
    }

    private List<LogDto> logsToList(Iterable<Document> iterable) {
        List<LogDto> logs = new ArrayList<>();
        iterable.forEach(document -> {
            LogDto logDto = new LogDto();
            logDto.setUrl(document.getString("url"));
            logDto.setCount(document.getInteger("count"));
            logDto.setRegion(document.getInteger("region"));
            logs.add(logDto);
        });

        return logs;
    }

    private RegionDto createRegionDto(int region, List<LogDto> url) {
        RegionDto regionDto = new RegionDto();
        regionDto.setRegion(region);
        regionDto.setTop3url(url);

        return regionDto;
    }

    private DateQueryDto createDateQueryDto(String date, List<LogDto> urls) {
        DateQueryDto dateDto = new DateQueryDto();
        dateDto.setDate(date);
        dateDto.setTop3url(urls);
        return dateDto;
    }
}