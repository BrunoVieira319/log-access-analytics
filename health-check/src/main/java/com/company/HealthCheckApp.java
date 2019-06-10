package com.company;

import com.codahale.metrics.health.HealthCheckRegistry;
import com.company.controller.HealthCheckController;
import com.company.healthcheck.DatabaseHealthCheck;
import com.company.healthcheck.ExternalServiceHealthCheck;
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
    public void run(HealthCheckConfiguration config, Environment environment) throws Exception {
        HealthCheckRegistry registry = new HealthCheckRegistry();
        registry.register("Metrics", new ExternalServiceHealthCheck("http://localhost:8081/healthcheck", config.getHttpClient()));
        registry.register("LogIngest", new ExternalServiceHealthCheck("http://localhost:8083/healthcheck", config.getHttpClient()));
        registry.register("MongoDb", new DatabaseHealthCheck(config.getMongoClient()));

        HealthCheckController controller = new HealthCheckController(registry);

        environment.jersey().register(controller);
    }
}
