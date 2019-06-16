package com.company.controller;

import com.company.service.HealthCheckService;
import com.company.util.LogFile;
import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.LoggerConfig;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/laaa")
@Produces(MediaType.APPLICATION_JSON)
public class HealthCheckController {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private HealthCheckService healthCheckService;

    public HealthCheckController(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
        LoggerConfig.of(logger).addHandler(LogFile.getLogFile());
    }

    @GET
    @Path("/health")
    public Response getStatus() {
        logger.atInfo().log("Endpoint GET laaa/health received a request");
        return healthCheckService.runHealthChecks();
    }

}