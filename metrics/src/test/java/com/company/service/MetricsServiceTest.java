package com.company.service;

import com.company.dao.BaseDao;
import com.company.dto.LogDto;
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

    public Iterable<Document>  generateLogsForTest() {
        List<Document> docs = new ArrayList<>();
        IntStream.range(1, 10).forEach(i -> docs.add(createDocument("id", "url/1", 1, i)));
        Collections.reverse(docs);
        return docs;
    }

    public Iterable<Document> generateLogsGroupedByRegion() {
        List<Document> docs = new ArrayList<>();
        IntStream.range(1, 10).forEach(i -> docs.add(createDocument("id", "url/1", 1, i)));
        IntStream.range(1, 10).forEach(i -> docs.add(createDocument("id", "url/2", 2, i)));
        IntStream.range(1, 10).forEach(i -> docs.add(createDocument("id", "url/3", 3, i)));
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
        when(metricsDao.getLogsGroupedByUrl()).thenReturn(generateLogsForTest());

        List<LogDto> top3Urls = metricsService.findMostAccessedUrls(3);
        LogDto firstLog = top3Urls.get(0);

        assertEquals(3, top3Urls.size());
        verify(metricsDao, times(1)).getLogsGroupedByUrl();
        assertEquals(Integer.valueOf(9), firstLog.getCount());
        assertEquals("url/1", firstLog.getUrl());
        assertEquals(Integer.valueOf(1), firstLog.getRegion());
    }

    @Test
    public void shouldFindLessAccessedUrls() {
        when(metricsDao.getLogsGroupedByUrl()).thenReturn(generateLogsForTest());

        List<LogDto> top3Urls = metricsService.findLessAccessedUrls(3);
        LogDto firstLog = top3Urls.get(0);

        assertEquals(3, top3Urls.size());
        verify(metricsDao, times(1)).getLogsGroupedByUrl();
        assertEquals(Integer.valueOf(1), firstLog.getCount());
        assertEquals("url/1", firstLog.getUrl());
        assertEquals(Integer.valueOf(1), firstLog.getRegion());
    }

    @Test
    public void shouldFindMostAccessedUrlsInEachRegion() {
    //    when(metricsDao.getLogsGroupedByRegionAndUrl()).thenReturn(generateLogsGroupedByRegion());

    }

    @Test
    public void shouldReturnAllLogsWithHighestValueCountEvenIfAmountIsHigherThanLimit() {
        List<Document> docs = new ArrayList<>();
        IntStream.range(1, 10).forEach(i -> docs.add(createDocument("id", "url/1", 1, 1)));
        when(metricsDao.getLogsGroupedByUrl()).thenReturn(docs);

        List<LogDto> top3Urls = metricsService.findMostAccessedUrls(3);

        assertEquals(top3Urls.size(), 9);
    }

    @Test
    public void shouldReturnEmptyListIfNothingWasFound() {
        when(metricsDao.getLogsGroupedByUrl()).thenReturn(new ArrayList<>());

        List<LogDto> top3Urls = metricsService.findMostAccessedUrls(3);
        assertTrue(top3Urls.isEmpty());
    }
}
