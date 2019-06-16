package com.company;

import com.company.controller.LogIngestController;
import com.company.dao.BaseDao;
import com.company.dao.LogIngestDao;
import com.company.healthcheck.LogIngestHealthCheck;
import com.company.service.LogIngestService;
import com.company.util.CustomExceptionMapper;
import com.company.util.LogFile;
import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.LoggerConfig;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class LogIngestApp extends Application<LogIngestConfiguration> {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    public static void main(String[] args) {
        LoggerConfig.of(logger).addHandler(LogFile.getLogFile());
        logger.atInfo().log("Starting Log Ingest Service");
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
        environment.jersey().register(new CustomExceptionMapper());
        environment.healthChecks().register("log", new LogIngestHealthCheck());
    }
}
