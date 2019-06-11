package com.company.service;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
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

    @InjectMocks
    HealthCheckService healthCheckService;

    @Before
    public void initHealthCheckServiceTest() {
        this.results = new TreeMap<>();
        this.healthCheckService = new HealthCheckService(registry);
    }

    @Test
    public void shouldReturnOkWhenRunHealthChecks() {
        when(registry.runHealthChecks()).thenReturn(results);
        Response response = healthCheckService.runHealthChecks();

        assertEquals(200, response.getStatus());
        verify(registry, times(1)).runHealthChecks();
    }

    @Test
    public void shouldReturnServerErrorWhenRunHealthChecks() {
        HealthCheck.Result result = mock(HealthCheck.Result.class);
        results.put("", result);

        when(registry.runHealthChecks()).thenReturn(results);
        Response response = healthCheckService.runHealthChecks();

        assertEquals(500, response.getStatus());
        verify(registry, times(1)).runHealthChecks();
    }

}
