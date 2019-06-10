package com.company.controller;

import com.codahale.metrics.health.HealthCheckRegistry;
import com.company.service.HealthCheckService;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

@RunWith(MockitoJUnitRunner.class)
public class HealthCheckControllerTest {

    @Mock
    HealthCheckService service;

    @Mock
    HealthCheckRegistry registry;

    @InjectMocks
    HealthCheckController healthCheckController;

    public HealthCheckControllerTest() {
        this.healthCheckController = new HealthCheckController(registry);
    }

    @Test
    public void shouldReturnStatusOkWhenCalled() {
        Response ok = mock(Response.class);

        when(service.runHealthChecks(any(HealthCheckRegistry.class)))
                .thenReturn(ok);

        Response status = healthCheckController.getStatus();

        verify(service, times(1)).runHealthChecks(any(HealthCheckRegistry.class));
        assertEquals(ok, status);
    }
}
