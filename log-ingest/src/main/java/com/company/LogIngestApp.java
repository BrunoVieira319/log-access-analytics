package com.company;

import com.company.controller.LogIngestController;
import com.company.healthcheck.LogIngestHealthCheck;
import com.company.service.LogIngestService;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class LogIngestApp extends Application<LogIngestConfiguration> {

    public static void main(String[] args) {
        try {
            new LogIngestApp().run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(LogIngestConfiguration config, Environment environment) throws Exception {
        LogIngestService logIngestService = new LogIngestService();
        LogIngestController logIngestController = new LogIngestController(logIngestService);

        environment.jersey().register(logIngestController);
        environment.healthChecks().register("log", new LogIngestHealthCheck());
    }
}
