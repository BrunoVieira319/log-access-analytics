package com.company.service;

import com.company.dao.BaseDao;
import com.company.dto.DateQueryDto;
import com.company.dto.LogDto;
import com.company.dto.RegionDto;
import com.company.util.DtoCreator;
import org.bson.Document;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MetricsService {

    private BaseDao metricsDao;

    public MetricsService(BaseDao metricsDao) {
        this.metricsDao = metricsDao;
    }

    public List<LogDto> findMostAccessedUrls(int limit) {
        Iterable<Document> bsonLogs = metricsDao.getLogsGroupedByUrl();
        List<LogDto> logs = documentsToDto(bsonLogs);

        return getTop(logs, limit);
    }

    public List<LogDto> findLessAccessedUrls(int limit) {
        Iterable<Document> bsonLogs = metricsDao.getLogsGroupedByUrl();
        List<LogDto> logs = documentsToDto(bsonLogs);
        Collections.reverse(logs);

        return getTop(logs, limit);
    }

    public List<RegionDto> findMostAccessedUrlsPerRegion(int limit) {
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
        Iterable<Document> logsGroupedByMinute = metricsDao.getLogsGroupedByMinute();
        Document minuteWithMoreAccess = logsGroupedByMinute.iterator().next();
        return minuteWithMoreAccess;
    }

    private Iterable<Document> findAccessedUrlsOnDay(String day) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate localDate = LocalDate.parse(day, formatter);

            return metricsDao.getLogsGroupedByUrlsAccessedBetween(localDate, localDate.plusDays(1));
        } catch (Exception e) {
            System.out.println("Parâmetro day inválido");
            return new ArrayList<>();
        }
    }

    private Iterable<Document> findAccessedUrlsInWeek(String weekYear) {
        try {
            String[] separatedWeekYear = weekYear.split("-");
            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            LocalDate firstWeekDay = LocalDate.now()
                    .withYear(Integer.valueOf(separatedWeekYear[1]))
                    .with(weekFields.weekOfYear(), Integer.valueOf(separatedWeekYear[0]))
                    .with(weekFields.dayOfWeek(), 1);

            LocalDate lastWeekDay = firstWeekDay.plusWeeks(1);

            return metricsDao.getLogsGroupedByUrlsAccessedBetween(firstWeekDay, lastWeekDay);
        } catch (Exception e) {
            System.out.println("Parâmetro week inválido");
            return new ArrayList<>();
        }
    }

    private Iterable<Document> findAccessedUrlsInYear(String year) {
        try {
            Year y = Year.of(Integer.valueOf(year));
            LocalDate firstDayOfTheYear = y.atDay(1);
            LocalDate lastDayOfTheYear = firstDayOfTheYear.plusYears(1);

            return metricsDao.getLogsGroupedByUrlsAccessedBetween(firstDayOfTheYear, lastDayOfTheYear);
        } catch (Exception e) {
            System.out.println("Parâmetro year inválido");
            return new ArrayList<>();
        }
    }

    private List<LogDto> getLogsFromRegion(int region, List<LogDto> logs) {
        return logs.stream()
                .filter(l -> l.getRegion() == region)
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

    private List<LogDto> documentsToDto(Iterable<Document> iterable) {
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