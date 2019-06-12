package com.company.dto;

import org.bson.Document;

import java.util.List;

public class MetricsDto {

    private List<LogDto> top3Urls;
    private List<RegionDto> top3UrlsPerRegion;
    private List<LogDto> lessAccessedUrl;
    private List<DateQueryDto> top3UrlsPerDates;
    private Document minuteWithMoreAccess;

    public List<LogDto> getTop3Urls() {
        return top3Urls;
    }

    public List<RegionDto> getTop3UrlsPerRegion() {
        return top3UrlsPerRegion;
    }

    public List<LogDto> getLessAccessedUrl() {
        return lessAccessedUrl;
    }

    public List<DateQueryDto> getTop3UrlsPerDates() {
        return top3UrlsPerDates;
    }

    public Document getMinuteWithMoreAccess() {
        return minuteWithMoreAccess;
    }

    public void setTop3Urls(List<LogDto> top3Urls) {
        this.top3Urls = top3Urls;
    }

    public void setTop3UrlsPerRegion(List<RegionDto> top3UrlsPerRegion) {
        this.top3UrlsPerRegion = top3UrlsPerRegion;
    }

    public void setLessAccessedUrl(List<LogDto> lessAccessedUrl) {
        this.lessAccessedUrl = lessAccessedUrl;
    }

    public void setTop3UrlsPerDates(List<DateQueryDto> top3UrlsPerDates) {
        this.top3UrlsPerDates = top3UrlsPerDates;
    }

    public void setMinuteWithMoreAccess(Document minuteWithMoreAccess) {
        this.minuteWithMoreAccess = minuteWithMoreAccess;
    }
}
