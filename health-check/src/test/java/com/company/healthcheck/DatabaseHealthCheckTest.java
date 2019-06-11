package com.company.healthcheck;

import com.codahale.metrics.health.HealthCheck;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseHealthCheckTest {

    @Mock
    MongoDatabase db;

    @Mock
    MongoClient mongoClient;

    @InjectMocks
    DatabaseHealthCheck databaseHealthCheck;

    @Test
    public void shouldReturnHealthyResultWhenCheckConnection() throws Exception {
        when(mongoClient.getDatabase(anyString())).thenReturn(db);
        when(db.runCommand(any(Document.class))).thenReturn(any());
        HealthCheck.Result result = databaseHealthCheck.check();

        assertTrue(result.isHealthy());
    }

    @Test
    public void shouldReturnUnhealthyResultWhenCheckConnection() throws Exception {
        when(mongoClient.getDatabase(anyString())).thenReturn(db);
        when(db.runCommand(any(Document.class))).thenThrow(RuntimeException.class);

        HealthCheck.Result result = databaseHealthCheck.check();

        assertFalse(result.isHealthy());
    }

}
