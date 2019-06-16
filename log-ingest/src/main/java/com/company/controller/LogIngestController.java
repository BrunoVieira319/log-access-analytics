package com.company.controller;

import com.company.service.LogIngestService;
import com.company.util.LogFile;
import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.LoggerConfig;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("/laar")
public class LogIngestController {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private final LogIngestService logIngestService;

    public LogIngestController(LogIngestService service) {
        this.logIngestService = service;
        LoggerConfig.of(logger).addHandler(LogFile.getLogFile());
    }

    @POST
    @Path("/ingest")
    @Consumes(MediaType.TEXT_PLAIN)
    public void saveLogs(String logs) {
        logger.atInfo().log("Endpoint POST /laar/ingest received a request");
        logIngestService.insertLogs(logs);
    }
}
