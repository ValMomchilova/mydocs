package com.val.mydocs.serivce;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DateTimeServiceImpl implements DateTimeService {
    @Override
    public LocalDate getCurrentDate() {
        return LocalDate.now();
    }
}
