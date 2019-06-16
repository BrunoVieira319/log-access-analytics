package com.company.healthcheck;

import com.codahale.metrics.health.HealthCheck;
import com.company.util.LogFile;
import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.LoggerConfig;

public class MetricsHealthCheck extends HealthCheck {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    public MetricsHealthCheck() {
        LoggerConfig.of(logger).addHandler(LogFile.getLogFile());
    }

    @Override
    public HealthCheck.Result check() {
        logger.atInfo().log("Checking service state through Metrics Health Check");
        return HealthCheck.Result.healthy();
    }
}
