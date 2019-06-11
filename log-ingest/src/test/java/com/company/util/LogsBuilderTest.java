package com.company.util;

import org.bson.Document;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LogsBuilderTest {

    @Test
    public void shouldFormatStringToLogs() {
        String logsAsString = "/pets/exotic/cats/10 1037825323957 5b019db5-b3d0-46d2-9963-437860af707f 1\n" +
                "/pets/guaipeca/dogs/1 1037825323957 5b019db5-b3d0-46d2-9963-437860af707g 2";

        List<Document> logs = LogsBuilder.build(logsAsString);
        Document firstLog = logs.get(0);

        assertEquals(logs.size(), 2);

        assertTrue(firstLog.containsKey("uuid"));
        assertEquals(firstLog.getString("uuid"), "5b019db5-b3d0-46d2-9963-437860af707f");

        assertTrue(firstLog.containsKey("url"));
        assertEquals(firstLog.getString("url"), "/pets/exotic/cats/10");

        assertTrue(firstLog.containsKey("region"));
        assertEquals(firstLog.getInteger("region"), Integer.valueOf(1));

        assertTrue(firstLog.containsKey("timestamp"));
        assertEquals(firstLog.getDate("timestamp"), new Date(1037825323957L));

    }
}
