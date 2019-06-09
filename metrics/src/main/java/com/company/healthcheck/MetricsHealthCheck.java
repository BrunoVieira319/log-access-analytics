package com.company.healthcheck;

import com.codahale.metrics.health.HealthCheck;

public class MetricsHealthCheck extends HealthCheck {

    @Override
    protected HealthCheck.Result check() throws Exception {
        return HealthCheck.Result.healthy();
    }
}
