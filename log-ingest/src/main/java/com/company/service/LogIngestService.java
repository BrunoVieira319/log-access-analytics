package com.company.service;

import com.company.dao.BaseDao;
import com.company.util.LogFile;
import com.company.util.LogsBuilder;
import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.LoggerConfig;
import org.bson.Document;

import java.util.List;

public class LogIngestService {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private BaseDao dao;

    public LogIngestService(BaseDao dao) {
        this.dao = dao;
        LoggerConfig.of(logger).addHandler(LogFile.getLogFile());
    }

    public void insertLogs(String logsAsString) {
        logger.atInfo().log("Inserting logs on database");
        List<Document> logs = LogsBuilder.build(logsAsString);
        dao.insertMany(logs);
    }
}
