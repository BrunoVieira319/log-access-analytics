package com.company;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class HealthCheckAppTest {

    @ClassRule
    public static final DropwizardAppRule<HealthCheckConfiguration> RULE =
            new DropwizardAppRule<>(HealthCheckApp.class, ResourceHelpers.resourceFilePath("config.yml"));

    @Test
    public void shouldTestEndpointWithARequest() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(String.format("http://localhost:%d/laaa/health", RULE.getLocalPort()))
                .build();

        Response response = client.newCall(request).execute();

        assertEquals(500, response.code());
    }
}