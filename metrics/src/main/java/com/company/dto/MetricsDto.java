package com.company.dto;

import org.bson.Document;
import java.util.List;

public class MetricsDto {

    private List<LogDto> top3WorldwideUrl;
    private List<RegionDto> top3RegionalUrl;
    private List<LogDto> lessAccessedUrl;
    private List<DateQueryDto> mostAccessedUrlPerDate;
    private Document mostAccessedTime;

    public List<LogDto> getTop3WorldwideUrl() {
        return top3WorldwideUrl;
    }

    public List<RegionDto> getTop3RegionalUrl() {
        return top3RegionalUrl;
    }

    public List<LogDto> getLessAccessedUrl() {
        return lessAccessedUrl;
    }

    public List<DateQueryDto> getMostAccessedUrlPerDate() {
        return mostAccessedUrlPerDate;
    }

    public Document getMostAccessedTime() {
        return mostAccessedTime;
    }

    public void setTop3WorldwideUrl(List<LogDto> top3WorldwideUrl) {
        this.top3WorldwideUrl = top3WorldwideUrl;
    }

    public void setTop3RegionalUrl(List<RegionDto> top3RegionalUrl) {
        this.top3RegionalUrl = top3RegionalUrl;
    }

    public void setLessAccessedUrl(List<LogDto> lessAccessedUrl) {
        this.lessAccessedUrl = lessAccessedUrl;
    }

    public void setMostAccessedUrlPerDate(List<DateQueryDto> mostAccessedUrlPerDate) {
        this.mostAccessedUrlPerDate = mostAccessedUrlPerDate;
    }

    public void setMostAccessedTime(Document mostAccessedTime) {
        this.mostAccessedTime = mostAccessedTime;
    }
}
