package com.company.controller;

import com.codahale.metrics.health.HealthCheckRegistry;
import com.company.service.HealthCheckService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/laaa")
@Produces(MediaType.APPLICATION_JSON)
public class HealthCheckController {

    private HealthCheckRegistry registry;
    private HealthCheckService service;

    public HealthCheckController(HealthCheckRegistry registry) {
        this.registry = registry;
        this.service = new HealthCheckService();
    }

    @GET
    @Path("/health")
    public Response getStatus() {
        return service.runHealthChecks(registry);
    }

}