package com.company;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class HealthCheckAppTest {

    @ClassRule
    public static final DropwizardAppRule<HealthCheckConfiguration> RULE =
            new DropwizardAppRule<>(HealthCheckApp.class, ResourceHelpers.resourceFilePath("config.yml"));

    @Test
    public void shouldTestEndpointWithRequest() {
        Client client = ClientBuilder.newClient();

        Response response = client
                .target(String.format("http://localhost:%d/laaa/health", RULE.getLocalPort()))
                .request()
                .get();

        assertEquals(500, response.getStatus());
    }
}