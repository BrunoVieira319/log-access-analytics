package com.company.controller;

import com.company.service.LogIngestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LogIngestControllerTest {

    @Mock
    LogIngestService logIngestService;

    @InjectMocks
    LogIngestController logIngestController;

    @Test
    public void shouldVerifyIfServiceWasCalled() {
        doNothing().when(logIngestService).insertLogs(any());
        logIngestController.saveLogs("1 2 3 4");

        verify(logIngestService, times(1)).insertLogs(any());
    }
}
