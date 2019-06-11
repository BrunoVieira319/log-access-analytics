package com.company.healthcheck;

import com.codahale.metrics.health.HealthCheck;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExternalServiceHealthCheckTest {

    @Mock
    OkHttpClient httpClient;

    @InjectMocks
    ExternalServiceHealthCheck externalServiceHealthCheck;

    @Mock
    Call call;

    @Mock
    Response response;

    public ExternalServiceHealthCheckTest() {
        externalServiceHealthCheck = new ExternalServiceHealthCheck("http://127.0.0.1", httpClient);
    }

    @Test
    public void shouldReturnHealthyResultWhenCheckOtherService() throws Exception {
        when(httpClient.newCall(any(Request.class))).thenReturn(call);
        when(call.execute()).thenReturn(response);
        when(response.isSuccessful()).thenReturn(true);

        HealthCheck.Result result = externalServiceHealthCheck.check();

        assertTrue(result.isHealthy());
    }

    @Test
    public void shouldReturnUnhealthyResultWhenCheckOtherService() throws Exception {
        when(httpClient.newCall(any(Request.class))).thenReturn(call);
        when(call.execute()).thenReturn(response);
        when(response.isSuccessful()).thenReturn(false);

        HealthCheck.Result result = externalServiceHealthCheck.check();

        assertFalse(result.isHealthy());
    }

}
