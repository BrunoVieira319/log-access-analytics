package com.company.controller;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.stream.Stream;

@Path("/laaa")
public class HealthCheckController {

    private final String defaultName;
    private final String template;
    private HealthCheckRegistry registry;

    public HealthCheckController(String defaultName, String template, HealthCheckRegistry registry) {
        this.defaultName = defaultName;
        this.template = template;
        this.registry = registry;
    }

    @GET
    @Path("/health")
    public Response getStatus() {
        Stream<HealthCheck.Result> result = registry.runHealthChecks().values().stream();
        Optional<HealthCheck.Result> unhealthy = result.filter(r -> !r.isHealthy()).findFirst();

        if (unhealthy.isPresent()) {
            return Response.status(500).build();
        };

        return Response.status(200).build();
    }

}