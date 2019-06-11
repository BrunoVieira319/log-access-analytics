package com.company.service;

import com.company.dao.BaseDao;
import com.company.util.LogsBuilder;
import org.bson.Document;

import java.util.List;

public class LogIngestService {

    private BaseDao dao;

    public LogIngestService(BaseDao dao) {
        this.dao = dao;
    }

    public void insertLogs(String logsAsString) {
        List<Document> logs = LogsBuilder.build(logsAsString);
        dao.insertMany(logs);
    }
}
