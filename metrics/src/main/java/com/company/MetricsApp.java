package com.company;

import com.company.controller.MetricsController;
import com.company.healthcheck.MetricsHealthCheck;
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
    public void run(MetricsConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(new MetricsController());
        environment.healthChecks().register("metrics", new MetricsHealthCheck());
    }
}
