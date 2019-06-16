package com.company.service;

import com.company.dao.BaseDao;
import com.company.dto.DateQueryDto;
import com.company.dto.LogDto;
import com.company.dto.RegionDto;
import com.company.util.DtoCreator;
import com.company.util.LogFile;
import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.LoggerConfig;
import com.google.common.flogger.StackSize;
import org.bson.Document;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MetricsService {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private BaseDao metricsDao;

    public MetricsService(BaseDao metricsDao) {
        this.metricsDao = metricsDao;
        LoggerConfig.of(logger).addHandler(LogFile.getLogFile());
    }

    public List<LogDto> findMostAccessedUrls(int limit) {
        logger.atInfo().log("Finding most accessed urls");
        Iterable<Document> bsonLogs = metricsDao.getLogsGroupedByUrl();
        List<LogDto> logs = documentsToDto(bsonLogs);

        return getTop(logs, limit);
    }

    public List<LogDto> findLessAccessedUrls(int limit) {
        logger.atInfo().log("Finding less accessed urls");
        Iterable<Document> bsonLogs = metricsDao.getLogsGroupedByUrl();
        List<LogDto> logs = documentsToDto(bsonLogs);
        Collections.reverse(logs);

        return getTop(logs, limit);
    }

    public List<RegionDto> findMostAccessedUrlsPerRegion(int limit) {
        logger.atInfo().log("Finding most accessed urls per region");
        Iterable<Document> bsonLogs = metricsDao.getLogsGroupedByRegionAndUrl();
        List<LogDto> allLogs = documentsToDto(bsonLogs);

        List<RegionDto> regions = new ArrayList<>();

        IntStream.range(1, 4).forEach(region -> {
            List<LogDto> urls = getLogsFromRegion(region, allLogs);
            RegionDto regionDto = DtoCreator.regionDto(region, getTop(urls, limit));

            regions.add(regionDto);
        });
        return regions;
    }

    public List<DateQueryDto> findMostAccessedUrlsPerDates(String day, String weekYear, String year, int limit) {
        logger.atInfo().log("Finding most accessed urls on day:%s, week:%s, year:%s", day, weekYear, year);
        Iterable<Document> logsOfDay = findAccessedUrlsOnDay(day);
        Iterable<Document> logsOfWeek = findAccessedUrlsInWeek(weekYear);
        Iterable<Document> logsOfYear = findAccessedUrlsInYear(year);

        List<LogDto> topOfTheDay = getTop(documentsToDto(logsOfDay), limit);
        List<LogDto> topOfTheWeek = getTop(documentsToDto(logsOfWeek), limit);
        List<LogDto> topOfTheYear = getTop(documentsToDto(logsOfYear), limit);

        DateQueryDto dayDto = DtoCreator.dateQueryDto("Day: " + day, topOfTheDay);
        DateQueryDto weekDto = DtoCreator.dateQueryDto("Week: " + weekYear, topOfTheWeek);
        DateQueryDto yearDto = DtoCreator.dateQueryDto("Year: " + year, topOfTheYear);

        return Arrays.asList(dayDto, weekDto, yearDto);
    }

    public Document findMinuteWithMoreAccess() {
        logger.atInfo().log("Finding the minute with more access in all urls");
        Iterable<Document> logsGroupedByMinute = metricsDao.getLogsGroupedByMinute();
        Document minuteWithMoreAccess = logsGroupedByMinute.iterator().next();
        return minuteWithMoreAccess;
    }

    private Iterable<Document> findAccessedUrlsOnDay(String day) {
        logger.atInfo().log("Parsing day %s", day);
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate localDate = LocalDate.parse(day, formatter);

            return metricsDao.getLogsGroupedByUrlsAccessedBetween(localDate, localDate.plusDays(1));
        } catch (Exception exception) {
            logger.atInfo()
                    .withStackTrace(StackSize.SMALL)
                    .log("Error on trying to parse the day %s", day);
            return new ArrayList<>();
        }
    }

    private Iterable<Document> findAccessedUrlsInWeek(String weekYear) {
        logger.atInfo().log("Parsing week %s", weekYear);
        try {
            String[] separatedWeekYear = weekYear.split("-");
            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            LocalDate firstWeekDay = LocalDate.now()
                    .withYear(Integer.valueOf(separatedWeekYear[1]))
                    .with(weekFields.weekOfYear(), Integer.valueOf(separatedWeekYear[0]))
                    .with(weekFields.dayOfWeek(), 1);

            LocalDate lastWeekDay = firstWeekDay.plusWeeks(1);

            return metricsDao.getLogsGroupedByUrlsAccessedBetween(firstWeekDay, lastWeekDay);
        } catch (Exception exception) {
            logger.atInfo()
                    .withStackTrace(StackSize.SMALL)
                    .log("Error on trying to parse the week %s", weekYear);
            return new ArrayList<>();
        }
    }

    private Iterable<Document> findAccessedUrlsInYear(String year) {
        logger.atInfo().log("Parsing year %s", year);
        try {
            Year yearClass = Year.of(Integer.valueOf(year));
            LocalDate firstDayOfTheYear = yearClass.atDay(1);
            LocalDate lastDayOfTheYear = firstDayOfTheYear.plusYears(1);

            return metricsDao.getLogsGroupedByUrlsAccessedBetween(firstDayOfTheYear, lastDayOfTheYear);
        } catch (Exception exception) {
            logger.atInfo()
                    .withStackTrace(StackSize.SMALL)
                    .log("Error on trying to parse the year %s", year);
            return new ArrayList<>();
        }
    }

    private List<LogDto> getLogsFromRegion(int region, List<LogDto> logs) {
        logger.atInfo().log("Getting logs from region %s", region);
        return logs.stream()
                .filter(l -> l.getRegion() == region)
                .collect(Collectors.toList());
    }

    private List<LogDto> getTop(List<LogDto> list, int limit) {
        logger.atInfo().log("Getting %s first logs in the list", limit);
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
        } catch (IndexOutOfBoundsException exception) {
            logger.atInfo()
                    .log("Possibly no one logs was found | %s", exception.getMessage());
            return list;
        } catch (Exception exception) {
            logger.atInfo()
                    .withStackTrace(StackSize.SMALL)
                    .log("Error on trying to get logs from list");
            return list;
        }
    }

    private List<LogDto> documentsToDto(Iterable<Document> iterable) {
        logger.atInfo().log("Converting Documents to LogsDto");
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