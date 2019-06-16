package com.company;

import com.codahale.metrics.health.HealthCheckRegistry;
import com.company.controller.HealthCheckController;
import com.company.healthcheck.DatabaseHealthCheck;
import com.company.healthcheck.ExternalServiceHealthCheck;
import com.company.service.HealthCheckService;
import com.company.util.CustomExceptionMapper;
import com.company.util.LogFile;
import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.LoggerConfig;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;


public class HealthCheckApp extends Application<HealthCheckConfiguration> {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    public static void main(String[] args) {
        try {
            LoggerConfig.of(logger).addHandler(LogFile.getLogFile());
            logger.atInfo().log("Starting Health Check Service");
            new HealthCheckApp().run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(HealthCheckConfiguration config, Environment environment) {
        HealthCheckRegistry registry = new HealthCheckRegistry();
        registry.register("Metrics", new ExternalServiceHealthCheck("http://localhost:8081/healthcheck", config.getHttpClient()));
        registry.register("LogIngest", new ExternalServiceHealthCheck("http://localhost:8083/healthcheck", config.getHttpClient()));
        registry.register("MongoDb", new DatabaseHealthCheck(config.getMongoClient()));

        HealthCheckService service = new HealthCheckService(registry);
        HealthCheckController controller = new HealthCheckController(service);

        environment.jersey().register(new CustomExceptionMapper());
        environment.jersey().register(controller);
    }
}
