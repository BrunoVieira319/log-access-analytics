package com.company.controller;

import com.company.service.HealthCheckService;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

@RunWith(MockitoJUnitRunner.class)
public class HealthCheckControllerTest {

    @Mock
    HealthCheckService service;

    @InjectMocks
    HealthCheckController healthCheckController;

    @Test
    public void shouldReturnStatusOkWhenCalled() {
        Response ok = mock(Response.class);

        when(service.runHealthChecks())
                .thenReturn(ok);

        Response status = healthCheckController.getStatus();

        verify(service, times(1)).runHealthChecks();
        assertEquals(ok, status);
    }

}
