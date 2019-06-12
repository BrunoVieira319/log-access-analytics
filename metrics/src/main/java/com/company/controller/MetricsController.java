package com.company.controller;

import com.codahale.metrics.annotation.Timed;
import com.company.dto.DateQueryDto;
import com.company.dto.LogDto;
import com.company.dto.MetricsDto;
import com.company.dto.RegionDto;
import com.company.service.MetricsService;
import com.company.util.DtoCreator;
import org.bson.Document;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/laa")
@Produces(MediaType.APPLICATION_JSON)
public class MetricsController {

    private final MetricsService metricsService;

    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @GET
    @Path("/metrics")
    @Timed
    public MetricsDto getMetrics(
            @NotNull @QueryParam("day") String day,
            @NotNull @QueryParam("week") String weekYear,
            @NotNull @QueryParam("year") String year
    ) {
        List<LogDto> mostAccessedUrls = metricsService.findMostAccessedUrls(3);
        List<RegionDto> mostAccessedUrlsPerRegion = metricsService.findMostAccessedUrlsPerRegion(3);
        List<LogDto> lessAccessedUrls = metricsService.findLessAccessedUrls(1);
        List<DateQueryDto> mostAccessedUrlsPerDates = metricsService.findMostAccessedUrlsPerDates(day, weekYear, year, 3);
        Document minuteWithMoreAccess = metricsService.findMinuteWithMoreAccess();

        MetricsDto metricsDto = DtoCreator.metricsDto(
                mostAccessedUrls,
                mostAccessedUrlsPerRegion,
                lessAccessedUrls,
                mostAccessedUrlsPerDates,
                minuteWithMoreAccess);

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
        return metricsService.findMostAccessedUrlsPerDates(day, weekYear, year, 3);
    }

    @GET
    @Path("/metrics/5")
    @Timed
    public Document getMostAccessedMinute() {
        return metricsService.findMinuteWithMoreAccess();
    }
}
