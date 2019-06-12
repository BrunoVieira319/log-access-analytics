package com.company.service;

import com.company.dao.BaseDao;
import com.company.dto.DateQueryDto;
import com.company.dto.LogDto;
import com.company.dto.RegionDto;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MetricsServiceTest {

    @Mock
    BaseDao metricsDao;

    @InjectMocks
    MetricsService metricsService;

    public Iterable<Document> getLogsForTesting() {
        List<Document> docs = new ArrayList<>();
        IntStream.range(1, 10).forEach(i -> docs.add(createDocument("id", "url/1", 1, i)));
        Collections.reverse(docs);
        return docs;
    }

    public Iterable<Document> getLogsFromDifferentRegionsForTesting() {
        List<Document> docs = new ArrayList<>();
        IntStream.range(1, 4).forEach(i ->
                IntStream.range(1, 10).forEach(j ->
                        docs.add(createDocument("id", "url/1", i, j))
                )
        );
        Collections.reverse(docs);
        return docs;
    }

    private Document createDocument(String id, String url, int region, int count) {
        return new Document("uuid", id)
                .append("url", url)
                .append("region", region)
                .append("count", count);
    }

    @Test
    public void shouldFindMostAccessedUrls() {
        when(metricsDao.getLogsGroupedByUrl()).thenReturn(getLogsForTesting());

        List<LogDto> top3Urls = metricsService.findMostAccessedUrls(3);
        assertEquals(3, top3Urls.size());
        verify(metricsDao, times(1)).getLogsGroupedByUrl();

        LogDto firstLog = top3Urls.get(0);

        assertEquals(Integer.valueOf(9), firstLog.getCount());
        assertEquals("url/1", firstLog.getUrl());
        assertEquals(Integer.valueOf(1), firstLog.getRegion());
    }

    @Test
    public void shouldFindLessAccessedUrls() {
        when(metricsDao.getLogsGroupedByUrl()).thenReturn(getLogsForTesting());

        List<LogDto> top3Urls = metricsService.findLessAccessedUrls(3);
        assertEquals(3, top3Urls.size());
        verify(metricsDao, times(1)).getLogsGroupedByUrl();

        LogDto firstLog = top3Urls.get(0);
        assertEquals(Integer.valueOf(1), firstLog.getCount());
        assertEquals("url/1", firstLog.getUrl());
        assertEquals(Integer.valueOf(1), firstLog.getRegion());
    }

    @Test
    public void shouldFindMostAccessedUrlsInEachRegion() {
        when(metricsDao.getLogsGroupedByRegionAndUrl()).thenReturn(getLogsFromDifferentRegionsForTesting());

        List<RegionDto> regions = metricsService.findMostAccessedUrlsPerRegion(3);
        assertEquals(3, regions.size());
        verify(metricsDao, times(1)).getLogsGroupedByRegionAndUrl();

        RegionDto firstRegion = regions.get(0);
        assertEquals(1, firstRegion.getRegion());
        assertEquals(3, firstRegion.getTopUrls().size());

        LogDto topUrl = firstRegion.getTopUrls().get(0);
        assertEquals(Integer.valueOf(9), topUrl.getCount());
    }

    @Test
    public void shouldFindMostAccessedUrlsGivenDayWeekAndYear() {
        when(metricsDao.getLogsGroupedByUrlsAccessedBetween(any(), any())).thenReturn(getLogsForTesting());

        List<DateQueryDto> dates = metricsService.findMostAccessedUrlsPerDates("01-01-2000", "20-2000", "2000", 3);
        assertEquals(3, dates.size());
        verify(metricsDao, times(3)).getLogsGroupedByUrlsAccessedBetween(any(), any());

        DateQueryDto day = dates.get(0);
        assertEquals("Day: 01-01-2000", day.getDate());
        assertEquals(3, day.getTopUrls().size());

        DateQueryDto week = dates.get(1);
        assertEquals("Week: 20-2000", week.getDate());
        assertEquals(3, week.getTopUrls().size());

        DateQueryDto year = dates.get(2);
        assertEquals("Year: 2000", year.getDate());
        assertEquals(3, year.getTopUrls().size());
    }

    @Test
    public void shouldReturnAEmptyListIfGivenInvalidDay() {
        when(metricsDao.getLogsGroupedByUrlsAccessedBetween(any(), any())).thenReturn(getLogsForTesting());

        List<DateQueryDto> dates = metricsService.findMostAccessedUrlsPerDates("546878", "20-2000", "2000", 3);
        DateQueryDto day = dates.get(0);
        assertTrue(day.getTopUrls().isEmpty());
    }

    @Test
    public void shouldReturnAEmptyListIfGivenInvalidWeek() {
        when(metricsDao.getLogsGroupedByUrlsAccessedBetween(any(), any())).thenReturn(getLogsForTesting());

        List<DateQueryDto> dates = metricsService.findMostAccessedUrlsPerDates("01-01-2000", "48", "2000", 3);
        DateQueryDto week = dates.get(1);
        assertTrue(week.getTopUrls().isEmpty());
    }

    @Test
    public void shouldReturnAEmptyListIfGivenInvalidYear() {
        when(metricsDao.getLogsGroupedByUrlsAccessedBetween(any(), any())).thenReturn(getLogsForTesting());

        List<DateQueryDto> dates = metricsService.findMostAccessedUrlsPerDates("01-01-2000", "20-2000", "abc", 3);
        DateQueryDto year = dates.get(2);
        assertTrue(year.getTopUrls().isEmpty());
    }

    @Test
    public void shouldFindMostAccessedMinuteOfTheDay() {
        when(metricsDao.getLogsGroupedByMinute()).thenReturn(getLogsForTesting());

        Document minute = metricsService.findMinuteWithMoreAccess();
        assertEquals(9, minute.get("count"));
        verify(metricsDao, times(1)).getLogsGroupedByMinute();
    }

    @Test
    public void shouldReturnAllLogsWithHighestValueCountEvenIfAmountIsHigherThanLimit() {
        List<Document> docs = new ArrayList<>();
        IntStream.range(1, 10).forEach(i -> docs.add(createDocument("id", "url/1", 1, 1)));
        when(metricsDao.getLogsGroupedByUrl()).thenReturn(docs);

        List<LogDto> top3Urls = metricsService.findMostAccessedUrls(3);
        assertEquals(9, top3Urls.size());
    }

    @Test
    public void shouldReturnEmptyListIfNothingWasFound() {
        when(metricsDao.getLogsGroupedByUrl()).thenReturn(new ArrayList<>());

        List<LogDto> top3Urls = metricsService.findMostAccessedUrls(3);
        assertTrue(top3Urls.isEmpty());
    }
}
