package com.company.util;

import com.company.dto.DateQueryDto;
import com.company.dto.LogDto;
import com.company.dto.MetricsDto;
import com.company.dto.RegionDto;
import org.bson.Document;

import java.util.List;

public class DtoCreator {

    public static RegionDto regionDto(int region, List<LogDto> url) {
        RegionDto regionDto = new RegionDto();
        regionDto.setRegion(region);
        regionDto.setTopUrls(url);

        return regionDto;
    }

    public static DateQueryDto dateQueryDto(String date, List<LogDto> urls) {
        DateQueryDto dateDto = new DateQueryDto();
        dateDto.setDate(date);
        dateDto.setTopUrls(urls);

        return dateDto;
    }

    public static LogDto logDto(Integer region, String url, Integer count) {
        LogDto logDto = new LogDto();
        logDto.setRegion(region);
        logDto.setUrl(url);
        logDto.setCount(count);

        return logDto;
    }

    public static MetricsDto metricsDto(
            List<LogDto> top3Urls,
            List<RegionDto> top3UrlsPerRegion,
            List<LogDto> lessAccessedUrl,
            List<DateQueryDto> top3UrlsPerDates,
            Document minuteWithMoreAccess) {

        MetricsDto metricsDto = new MetricsDto();

        metricsDto.setTop3Urls(top3Urls);
        metricsDto.setTop3UrlsPerRegion(top3UrlsPerRegion);
        metricsDto.setLessAccessedUrl(lessAccessedUrl);
        metricsDto.setTop3UrlsPerDates(top3UrlsPerDates);
        metricsDto.setMinuteWithMoreAccess(minuteWithMoreAccess);

        return metricsDto;
    }
}
