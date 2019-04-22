package com.val.mydocs.unit.services;

import com.val.mydocs.serivce.DateTimeService;
import com.val.mydocs.serivce.DateTimeServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class DateTimeServiceImplTests {

    @Test
    public void getCurrentDateGetsCurrentDate(){
        DateTimeService dateTimeService = new DateTimeServiceImpl();
        LocalDate now = LocalDate.now();
        assertEquals(now, dateTimeService.getCurrentDate());
    }
}
