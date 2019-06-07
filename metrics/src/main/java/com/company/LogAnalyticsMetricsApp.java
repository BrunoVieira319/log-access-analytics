package com.company;

import com.company.controller.MetricsController;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class LogAnalyticsMetricsApp extends Application<LogAnalyticsMetricsConfiguration> {

    public static void main(String[] args) {
        try {
            new LogAnalyticsMetricsApp().run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(LogAnalyticsMetricsConfiguration configuration, Environment environment) throws Exception {
        MetricsController metricsController = new MetricsController(
                configuration.getDefaultName(),
                configuration.getTemplate()
        );

        environment.jersey().register(metricsController);
    }
}
