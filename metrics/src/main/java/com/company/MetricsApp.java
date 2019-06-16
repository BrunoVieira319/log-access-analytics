package com.company;

import com.company.controller.MetricsController;
import com.company.dao.BaseDao;
import com.company.dao.MetricsDao;
import com.company.healthcheck.MetricsHealthCheck;
import com.company.service.MetricsService;
import com.company.util.CustomExceptionMapper;
import com.company.util.LogFile;
import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.LoggerConfig;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class MetricsApp extends Application<MetricsConfiguration> {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    public static void main(String[] args) {
        LoggerConfig.of(logger).addHandler(LogFile.getLogFile());
        logger.atInfo().log("Starting Metrics Service");
        try {
            new MetricsApp().run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(MetricsConfiguration config, Environment environment) {
        BaseDao metricsDao = new MetricsDao(config.getMongoClient());
        MetricsService metricsService = new MetricsService(metricsDao);
        MetricsController metricsController = new MetricsController(metricsService);

        environment.jersey().register(metricsController);
        environment.jersey().register(new CustomExceptionMapper());
        environment.healthChecks().register("metrics", new MetricsHealthCheck());
    }
}
