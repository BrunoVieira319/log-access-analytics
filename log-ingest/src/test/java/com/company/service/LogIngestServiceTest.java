package com.company.service;

import com.company.dao.BaseDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LogIngestServiceTest {

    @Mock
    BaseDao baseDao;

    @InjectMocks
    LogIngestService logIngestService;

    @Test
    public void shouldVerifyIfDaoWasCalled() {
        doNothing().when(baseDao).insertMany(anyList());
        logIngestService.insertLogs("1 2 3 4");

        verify(baseDao, times(1)).insertMany(anyList());
    }
}
