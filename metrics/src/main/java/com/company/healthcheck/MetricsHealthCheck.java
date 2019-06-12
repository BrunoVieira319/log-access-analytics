package com.company.healthcheck;

import com.codahale.metrics.health.HealthCheck;
import com.google.common.flogger.FluentLogger;

public class MetricsHealthCheck extends HealthCheck {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    @Override
    public HealthCheck.Result check() {
        logger.atInfo().log("Checking service state through Metrics Health Check");
        return HealthCheck.Result.healthy();
    }
}
