package com.company;

import com.codahale.metrics.health.HealthCheckRegistry;
import com.company.controller.HealthCheckController;
import com.company.healthcheck.DatabaseHealthCheck;
import com.company.healthcheck.LogIngestHealthCheck;
import com.company.healthcheck.MetricsHealthCheck;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class HealthCheckApp extends Application<HealthCheckConfiguration> {

    public static void main(String[] args) {
        try {
            new HealthCheckApp().run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(HealthCheckConfiguration configuration, Environment environment) throws Exception {
        HealthCheckRegistry registry = new HealthCheckRegistry();
        registry.register("Metrics", new MetricsHealthCheck());
        registry.register("LogIngest", new LogIngestHealthCheck());
        registry.register("Database", new DatabaseHealthCheck());

        HealthCheckController controller = new HealthCheckController(
                configuration.getDefaultName(),
                configuration.getTemplate(),
                registry
        );

        environment.jersey().register(controller);
    }
}
