package com.company.controller;

import com.company.service.LogService;
import com.company.util.LogsBuilder;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("/laar")
public class LogController {

    private final String template;
    private final String defaultName;
    private final LogService logService;

    public LogController(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.logService = new LogService();
    }

    @POST
    @Path("/ingest")
    @Consumes(MediaType.TEXT_PLAIN)
    public void saveLogs(String logs) {
        logService.insertLogs(LogsBuilder.build(logs));
    }
}
