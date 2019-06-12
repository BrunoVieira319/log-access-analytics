package com.company.controller;

import com.company.dto.DateQueryDto;
import com.company.dto.LogDto;
import com.company.dto.MetricsDto;
import com.company.dto.RegionDto;
import com.company.service.MetricsService;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MetricsControllerTest {

    @Mock
    MetricsService metricsService;

    @InjectMocks
    MetricsController metricsController;

    @Mock
    List<LogDto> logs;

    @Mock
    List<LogDto> otherLogs;

    @Mock
    List<RegionDto> regions;

    @Mock
    List<DateQueryDto> dates;

    @Mock
    Document minute;

    @Before
    public void mockingServiceMethods() {
        when(metricsService.findMostAccessedUrls(anyInt())).thenReturn(logs);
        when(metricsService.findLessAccessedUrls(anyInt())).thenReturn(otherLogs);
        when(metricsService.findMostAccessedUrlsPerRegion(anyInt())).thenReturn(regions);
        when(metricsService.findMostAccessedUrlsPerDates(anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(dates);
        when(metricsService.findMinuteWithMoreAccess()).thenReturn(minute);
    }

    @Test
    public void shouldReturnAllMetrics() {
        MetricsDto metrics = metricsController.getMetrics("01-01-2000", "23-2000", "2000");

        assertEquals(logs, metrics.getTop3Urls());
        assertEquals(regions, metrics.getTop3UrlsPerRegion());
        assertEquals(otherLogs, metrics.getLessAccessedUrl());
        assertEquals(dates, metrics.getTop3UrlsPerDates());
        assertEquals(minute, metrics.getMinuteWithMoreAccess());
        verify(metricsService, times(1)).findMostAccessedUrls(anyInt());
        verify(metricsService, times(1)).findMostAccessedUrlsPerRegion(anyInt());
        verify(metricsService, times(1)).findLessAccessedUrls(anyInt());
        verify(metricsService, times(1))
                .findMostAccessedUrlsPerDates(anyString(), anyString(), anyString(), anyInt());
        verify(metricsService, times(1)).findMinuteWithMoreAccess();

    }

    @Test
    public void shouldReturnMetric1() {
        List<LogDto> result = metricsController.getMostAccessedUrls();
        assertEquals(logs, result);
        verify(metricsService, times(1)).findMostAccessedUrls(anyInt());
    }

    @Test
    public void shouldReturnMetric2() {
        List<RegionDto> result = metricsController.getMostAccessedUrlsPerRegion();
        assertEquals(regions, result);
        verify(metricsService, times(1)).findMostAccessedUrlsPerRegion(anyInt());
    }

    @Test
    public void shouldReturnMetric3() {
        List<LogDto> result = metricsController.getLessAccessedUrls();
        assertEquals(otherLogs, result);
        verify(metricsService, times(1)).findLessAccessedUrls(anyInt());
    }

    @Test
    public void shouldReturnMetric4() {
        List<DateQueryDto> result = metricsController.getDateMetrics("01-01-2000", "23-2000", "2000");
        assertEquals(dates, result);
        verify(metricsService, times(1))
                .findMostAccessedUrlsPerDates(anyString(), anyString(), anyString(), anyInt());
    }

    @Test
    public void shouldReturnMetric5() {
        Document result = metricsController.getMostAccessedMinute();
        assertEquals(minute, result);
        verify(metricsService, times(1)).findMinuteWithMoreAccess();
    }
}
