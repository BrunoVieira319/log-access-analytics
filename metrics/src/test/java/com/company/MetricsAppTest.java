package com.company;

import com.company.db.EmbeddedMongoDb;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class MetricsAppTest {

    EmbeddedMongoDb db;

    @Before
    public void configureMongoForTests() {
        db = new EmbeddedMongoDb();
        db.start("localhost", 27017);
    }

    @After
    public void closeConnections() {
        db.close();
    }

    @ClassRule
    public static final DropwizardAppRule<MetricsConfiguration> RULE =
            new DropwizardAppRule<>(MetricsApp.class, ResourceHelpers.resourceFilePath("config.yml"));

    @Test
    public void shouldTestEndpointWithRequest() {
        Client client = ClientBuilder.newClient();

        Response response = client
                .target(String.format("http://localhost:%d/laa/metrics/1", RULE.getLocalPort()))
                .request()
                .get();

        assertEquals(200, response.getStatus());
    }
}

