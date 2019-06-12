package com.company;

import com.company.controller.MetricsController;
import com.company.dao.BaseDao;
import com.company.dao.MetricsDao;
import com.company.healthcheck.MetricsHealthCheck;
import com.company.service.MetricsService;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class MetricsApp extends Application<MetricsConfiguration> {

    public static void main(String[] args) {
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
        environment.healthChecks().register("metrics", new MetricsHealthCheck());
    }
}
