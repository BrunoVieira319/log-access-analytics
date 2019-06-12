package com.company;

import com.mongodb.MongoClient;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LogIngestConfigurationTest {

    LogIngestConfiguration configuration;

    @Before
    public void initLogIngestConfiguration() {
        configuration = new LogIngestConfiguration();
    }

    @Test
    public void shouldReturnMongoClient() {
        MongoClient mongoClient = configuration.getMongoClient();
        assertNotNull(mongoClient);
    }

    @Test
    public void shouldReturnDefaultName() {
        String defaultName = configuration.getDefaultName();
        assertEquals("LogIngest", defaultName);
    }
}
