package com.company.controller;

import com.codahale.metrics.annotation.Timed;
import com.company.dto.DateQueryDto;
import com.company.dto.LogDto;
import com.company.dto.MetricsDto;
import com.company.dto.RegionDto;
import com.company.service.MetricsService;
import org.bson.Document;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

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
    public MetricsDto getMetrics(
            @QueryParam("day") String day, @QueryParam("week") String weekYear, @QueryParam("year") String year
    ) {
        MetricsDto metricsDto = new MetricsDto();

        metricsDto.setTop3WorldwideUrl(metricsService.findMostAccessedUrls(3));
        metricsDto.setTop3RegionalUrl(metricsService.findMostAccessedUrlsPerRegion(3));
        metricsDto.setLessAccessedUrl(metricsService.findLessAccessedUrls(1));
        metricsDto.setMostAccessedUrlPerDate(metricsService.findMostAccessedUrlsPerDates(day, weekYear, year,3));
        metricsDto.setMostAccessedTime(metricsService.findMostAccessedMinute());

        return metricsDto;
    }

    @GET
    @Path("/metrics/1")
    @Timed
    public List<LogDto> getMostAccessedUrls() {
        return metricsService.findMostAccessedUrls(3);
    }

    @GET
    @Path("/metrics/2")
    @Timed
    public List<RegionDto> getMostAccessedUrlsPerRegion() {
        return metricsService.findMostAccessedUrlsPerRegion(3);
    }

    @GET
    @Path("/metrics/3")
    @Timed
    public List<LogDto> getLessAccessedUrls() {
        return metricsService.findLessAccessedUrls(1);
    }

    @GET
    @Path("/metrics/4")
    @Timed
    public List<DateQueryDto> getDateMetrics(
            @QueryParam("day") String day, @QueryParam("week") String weekYear, @QueryParam("year") String year
    ) {
        return metricsService.findMostAccessedUrlsPerDates(day, weekYear, year,3);
    }

    @GET
    @Path("/metrics/5")
    @Timed
    public Document getMostAccessedMinute() {
        return metricsService.findMostAccessedMinute();
    }
}
