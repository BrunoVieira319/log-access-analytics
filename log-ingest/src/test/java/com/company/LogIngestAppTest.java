package com.company;

import com.company.db.EmbeddedMongoDb;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.stream.IntStream;

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;
import static org.junit.Assert.assertEquals;

public class LogIngestAppTest {

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
    public static final DropwizardAppRule<LogIngestConfiguration> RULE =
            new DropwizardAppRule<>(LogIngestApp.class, resourceFilePath("config.yml"));

    @Test
    public void shouldTestEndpointWithRequest() {
        Client client = ClientBuilder.newClient();

        Entity<String> entity = Entity.entity(
                "/pets/exotic/cats/10 1037825323957 5b019db5-b3d0-46d2-9963-437860af707f 1",
                MediaType.TEXT_PLAIN_TYPE);

        waitMongoStart();

        Response response = client
                .target(String.format("http://localhost:%d/laar/ingest", RULE.getLocalPort()))
                .request()
                .post(entity);

        assertEquals(204, response.getStatus());
    }

    private void waitMongoStart() {
        IntStream.range(0,5).forEach(i -> {
            if (db.getMongoClient() == null) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
