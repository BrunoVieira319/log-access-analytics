package com.company;

import com.company.controller.LogIngestController;
import com.company.dao.BaseDao;
import com.company.dao.LogIngestDao;
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
    public void run(LogIngestConfiguration config, Environment environment) {
        BaseDao logIngestDao = new LogIngestDao(config.getMongoClient());
        LogIngestService logIngestService = new LogIngestService(logIngestDao);
        LogIngestController logIngestController = new LogIngestController(logIngestService);

        environment.jersey().register(logIngestController);
        environment.healthChecks().register("log", new LogIngestHealthCheck());
    }
}
