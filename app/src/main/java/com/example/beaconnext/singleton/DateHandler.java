package com.example.beaconnext.singleton;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateHandler {
    public static String utcConverter(String dateTime) {
        //dateTime.stripTrailing()
        //String inputTime = "2022-01-31T12:34:56"; // Replace this with your actual time

        // Parse the input time
        LocalDateTime istTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // Convert to UTC
        ZoneId istZone = ZoneId.of("Asia/Kolkata");
        ZoneId utcZone = ZoneId.of("UTC");

        ZonedDateTime istDateTime = ZonedDateTime.of(istTime, istZone);
        ZonedDateTime utcDateTime = istDateTime.withZoneSameInstant(utcZone);

        // Format the UTC time
        DateTimeFormatter outputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String outputTime = utcDateTime.format(outputFormatter);
        return outputTime;


    }
    public static String IstConverter(String dateTime) {

        ZoneId istZone = ZoneId.of("Asia/Kolkata");

        ZonedDateTime utcZonedDateTime = ZonedDateTime.parse(dateTime);

        ZonedDateTime istDateTime = utcZonedDateTime.withZoneSameInstant(istZone);

        String outputTime;
        DateTimeFormatter newFormatter=DateTimeFormatter.ofPattern("MM/dd-HH:mm");
        outputTime= istDateTime.format(newFormatter);
        return outputTime;


    }


}
