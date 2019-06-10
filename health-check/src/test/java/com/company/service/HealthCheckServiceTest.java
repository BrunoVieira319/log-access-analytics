package com.company.service;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HealthCheckServiceTest {

    @Mock
    HealthCheckRegistry registry;

    SortedMap<String, HealthCheck.Result> results;

    HealthCheckService healthCheckService;

    public HealthCheckServiceTest() {
        this.results = new TreeMap<>();
        this.healthCheckService = new HealthCheckService();
    }

    @Test
    public void shouldReturnOkWhenRunHealthChecks() {
        when(registry.runHealthChecks()).thenReturn(results);
        Response response = healthCheckService.runHealthChecks(registry);

        assertEquals(200, response.getStatus());
        verify(registry, times(1)).runHealthChecks();
    }

    @Test
    public void shouldReturnServerErrorWhenRunHealthChecks() {
        HealthCheck.Result result = mock(HealthCheck.Result.class);
        results.put("", result);

        when(registry.runHealthChecks()).thenReturn(results);
        Response response = healthCheckService.runHealthChecks(registry);

        assertEquals(500, response.getStatus());
        verify(registry, times(1)).runHealthChecks();
    }
}
