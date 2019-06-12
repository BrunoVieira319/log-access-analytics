package com.company.controller;

import com.company.service.HealthCheckService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/laaa")
@Produces(MediaType.APPLICATION_JSON)
public class HealthCheckController {

    private HealthCheckService healthCheckService;

    public HealthCheckController(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    @GET
    @Path("/health")
    public Response getStatus() {
        return healthCheckService.runHealthChecks();
    }

}