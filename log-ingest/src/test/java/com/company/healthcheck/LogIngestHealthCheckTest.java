package com.company.healthcheck;

import com.codahale.metrics.health.HealthCheck;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class LogIngestHealthCheckTest {


    LogIngestHealthCheck healthCheck;

    @Before
    public void initLogIngestHealthCheck() {
        healthCheck = new LogIngestHealthCheck();
    }

    @Test
    public void shouldReturnHealthy() throws Exception {
        HealthCheck.Result result = healthCheck.check();

        assertTrue(result.isHealthy());
    }

}
