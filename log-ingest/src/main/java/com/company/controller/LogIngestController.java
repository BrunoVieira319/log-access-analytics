package com.company.controller;

import com.company.service.LogIngestService;
import com.company.util.LogsBuilder;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("/laar")
public class LogIngestController {

    private final LogIngestService logIngestService;

    public LogIngestController(LogIngestService service) {
        this.logIngestService = service;
    }

    @POST
    @Path("/ingest")
    @Consumes(MediaType.TEXT_PLAIN)
    public void saveLogs(String logs) {
        logIngestService.insertLogs(logs);
    }
}
