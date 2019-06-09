package com.company.healthcheck;

import com.codahale.metrics.health.HealthCheck;

public class LogHealthCheck extends HealthCheck {

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}
