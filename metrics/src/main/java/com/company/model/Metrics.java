package com.company.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.client.AggregateIterable;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Metrics {

    List<Document> top3WorldwideUrl = new ArrayList<>();
    List<Document> top3RegionalUrl = new ArrayList<>();

    @JsonProperty
    public List<Document> getTop3WorldwideUrl() {
        return top3WorldwideUrl;
    }

    public void setTop3WorldwideUrl(AggregateIterable<Document> top3WorldwideUrl) {
        top3WorldwideUrl.forEach((Consumer<? super Document>) this.top3WorldwideUrl::add);
    }

    @JsonProperty
    public List<Document> getTop3RegionalUrl() {
        return top3RegionalUrl;
    }

    public void setTop3RegionalUrl(AggregateIterable<Document> top3RegionalUrl) {
        top3RegionalUrl.forEach((Consumer<? super Document>) this.top3RegionalUrl::add);
    }
}
