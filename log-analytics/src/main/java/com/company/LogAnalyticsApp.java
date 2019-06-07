package com.company;

import com.company.controller.LogController;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class LogAnalyticsApp extends Application<LogAnalyticsConfiguration> {

    public static void main(String[] args) {
        try {
            new LogAnalyticsApp().run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(LogAnalyticsConfiguration config, Environment environment) throws Exception {
        LogController logController = new LogController(
                config.getTemplate(),
                config.getDefaultName()
        );

        environment.jersey().register(logController);
    }
}
