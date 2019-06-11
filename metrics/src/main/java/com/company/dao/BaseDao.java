package com.company.dao;

import org.bson.Document;

import java.time.LocalDate;

public interface BaseDao {

    Iterable<Document> getLogsGroupedByUrl();
    Iterable<Document> getLogsGroupedByRegionAndUrl();
    Iterable<Document> getLogsGroupedByUrlsAccessedBetween(LocalDate initialDate, LocalDate finalDate);
    Iterable<Document> getLogsGroupedByMinute();
}
