package com.company.healthcheck;

import com.codahale.metrics.health.HealthCheck;
import com.company.util.LogFile;
import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.LoggerConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

public class ExternalServiceHealthCheck extends HealthCheck {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private Client client;
    private String url;

    public ExternalServiceHealthCheck(String url, Client client) {
        this.client = client;
        this.url = url;
        LoggerConfig.of(logger).addHandler(LogFile.getLogFile());
    }

    @Override
    protected Result check() {
        logger.atInfo().log("Verifying HealthCheck at %s", url );
        Response response = client
                .target(url)
                .request()
                .get();

        if (response.getStatus() == 200) {
            return Result.healthy();
        }
        return Result.unhealthy("Failed to connect to " + url);
    }
}
