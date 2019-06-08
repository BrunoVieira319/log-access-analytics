package com.company.controller;

import com.codahale.metrics.annotation.Timed;
import com.company.model.Metrics;
import com.company.service.MetricsService;
import com.mongodb.client.AggregateIterable;
import org.bson.Document;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/laa")
@Produces(MediaType.APPLICATION_JSON)
public class MetricsController {

    private final String template;
    private final String defaultName;
    private final MetricsService metricsService;

    public MetricsController(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.metricsService = new MetricsService();
    }

    @GET
    @Path("/metrics")
    @Timed
    public Metrics getMetrics() {
        Metrics metrics = new Metrics();

        metrics.setTop3WorldwideUrl(metricsService.findUrlsMostVisited(3));
        metrics.setTop3RegionalUrl(metricsService.findUrlsMostVisitedPerRegion(3));
        metrics.setLessAccessedUrl(metricsService.findUrlsLessVisited(1));
        return metrics;
    }
}
