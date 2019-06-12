package com.company.healthcheck;

import com.codahale.metrics.health.HealthCheck;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExternalServiceHealthCheckTest {

    @Mock
    Client httpClient;

    @InjectMocks
    ExternalServiceHealthCheck externalServiceHealthCheck;

    @Mock
    WebTarget webTarget;

    @Mock
    Builder builder;

    @Mock
    Response response;

    @Before
    public void initExternalServiceHealthCheck() {
        externalServiceHealthCheck = new ExternalServiceHealthCheck("http://127.0.0.1", httpClient);
    }

    @Before
    public void mockingCallRequest() {
        when(httpClient.target(anyString())).thenReturn(webTarget);
        when(webTarget.request()).thenReturn(builder);
        when(builder.get()).thenReturn(response);
    }

    @Test
    public void shouldReturnHealthyResultWhenCheckOtherService() {
        when(response.getStatus()).thenReturn(200);

        HealthCheck.Result result = externalServiceHealthCheck.check();
        assertTrue(result.isHealthy());
    }

    @Test
    public void shouldReturnUnhealthyResultWhenCheckOtherService() {
        HealthCheck.Result result = externalServiceHealthCheck.check();
        assertFalse(result.isHealthy());
    }

}
