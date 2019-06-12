package com.company.service;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.google.common.flogger.FluentLogger;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

public class HealthCheckService {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private HealthCheckRegistry registry;

    public HealthCheckService(HealthCheckRegistry registry) {
        this.registry = registry;
    }

    public Response runHealthChecks() {
        logger.atInfo().log("Running registered HealthChecks");
        SortedMap<String, HealthCheck.Result> results = registry.runHealthChecks();
        List<String> unhealthyResults = extractUnhealthyResults(results);

        if (unhealthyResults.isEmpty()) {
            return Response.ok().build();
        }
        return Response.serverError().entity(unhealthyResults).build();
    }

    private List<String> extractUnhealthyResults(SortedMap<String, HealthCheck.Result> results) {
        List<String> unhealthyResults = new ArrayList<>();

        results.forEach((check, result) -> {
            if(!result.isHealthy()) {
                String response = check + ": " + result.getMessage();
                unhealthyResults.add(response);
            }
        });
        return unhealthyResults;
    }
}
