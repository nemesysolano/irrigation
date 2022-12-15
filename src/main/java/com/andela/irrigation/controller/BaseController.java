package com.andela.irrigation.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

public class BaseController {
    private final SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    protected Date timeToDateTime(LocalTime time) {
        try {
            return timeFormatter.parse(
                    String.format("1970-01-01 0%2d:%02d:%02d",
                            time.getHour(),
                            time.getMinute(),
                            time.getSecond()
                    ));
        } catch (ParseException e) {
            return null;
        }
    }
}

