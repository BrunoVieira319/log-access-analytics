package com.company.service;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import org.jetbrains.annotations.NotNull;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

public class HealthCheckService {

    public Response runHealthChecks(HealthCheckRegistry registry) {
        SortedMap<String, HealthCheck.Result> results = registry.runHealthChecks();
        List<String> unhealthyResults = extractUnhealthyResults(results);

        if (unhealthyResults.isEmpty()) {
            return Response.ok().build();
        }
        return Response.serverError().entity(unhealthyResults).build();
    }

    @NotNull
    private List<String> extractUnhealthyResults(SortedMap<String, HealthCheck.Result> results) {
        List<String> unhealthyResults = new ArrayList<>();

        results.forEach((check, result) -> {
            if(!result.isHealthy()) {
                String response = check + ": " + result.getMessage();
                System.out.println(response);
                unhealthyResults.add(response);
            }
        });
        return unhealthyResults;
    }
}
