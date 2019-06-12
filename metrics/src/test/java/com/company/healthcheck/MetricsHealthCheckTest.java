package com.company.healthcheck;

import com.codahale.metrics.health.HealthCheck;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MetricsHealthCheckTest {

    MetricsHealthCheck metricsHealthCheck;

    @Before
    public void initMetricsHealthCheck() {
        metricsHealthCheck = new MetricsHealthCheck();
    }

    @Test
    public void shouldReturnHealthy() throws Exception {
        HealthCheck.Result result = metricsHealthCheck.check();
        assertTrue(result.isHealthy());
    }
}
