package com.company.util;

import com.company.dto.DateQueryDto;
import com.company.dto.LogDto;
import com.company.dto.MetricsDto;
import com.company.dto.RegionDto;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class DtoCreatorTest {

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

    @Test
    public void shouldCreateLogDto() {
        LogDto logDto = DtoCreator.logDto(1, "url", 10);

        assertEquals(Integer.valueOf(1), logDto.getRegion());
        assertEquals("url", logDto.getUrl());
        assertEquals(Integer.valueOf(10), logDto.getCount());
    }

    @Test
    public void shouldCreateRegionDto() {
        RegionDto regionDto = DtoCreator.regionDto(1, logs);

        assertEquals(1, regionDto.getRegion());
        assertEquals(logs, regionDto.getTopUrls());
    }

    @Test
    public void shouldCreateDatesDto() {
        DateQueryDto dateQueryDto = DtoCreator.dateQueryDto("Day: 01-01-2000", logs);

        assertEquals("Day: 01-01-2000", dateQueryDto.getDate());
        assertEquals(logs, dateQueryDto.getTopUrls());
    }

    @Test
    public void shouldCreateMetricsDto() {
        MetricsDto metricsDto = DtoCreator.metricsDto(logs, regions, otherLogs, dates, minute);

        assertEquals(logs, metricsDto.getTop3Urls());
        assertEquals(regions, metricsDto.getTop3UrlsPerRegion());
        assertEquals(otherLogs, metricsDto.getLessAccessedUrl());
        assertEquals(dates, metricsDto.getTop3UrlsPerDates());
        assertEquals(minute, metricsDto.getMinuteWithMoreAccess());
    }
}
