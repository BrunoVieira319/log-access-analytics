package com.company.model;

import com.company.dto.LogDto;
import com.company.dto.RegionDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.client.AggregateIterable;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Metrics {

    List<LogDto> top3WorldwideUrl = new ArrayList<>();
    List<RegionDto> top3RegionalUrl = new ArrayList<>();
    List<LogDto> lessAccessedUrl = new ArrayList<>();

    @JsonProperty
    public List<LogDto> getTop3WorldwideUrl() {
        return top3WorldwideUrl;
    }

    @JsonProperty
    public List<RegionDto> getTop3RegionalUrl() {
        return top3RegionalUrl;
    }

    @JsonProperty
    public List<LogDto> getLessAccessedUrl() {
        return lessAccessedUrl;
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


}
