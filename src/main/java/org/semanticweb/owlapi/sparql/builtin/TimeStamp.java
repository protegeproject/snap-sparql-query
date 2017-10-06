package org.semanticweb.owlapi.sparql.builtin;

import java.util.Optional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 06/06/15
 */
public class Timestamp {

    public static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";

    private static final DateFormat formatter = new SimpleDateFormat(PATTERN);


    private final long timestamp;

    private final Date date;


    public Timestamp(long timestamp) {
        this.timestamp = timestamp;
        this.date = new Date(timestamp);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public static Optional<Timestamp> parseDateTime(String dateTime) {
        try {
            Date date = formatter.parse(dateTime);
            return Optional.of(new Timestamp(date.getTime()));
        } catch (ParseException e) {
            return Optional.empty();
        }
    }

    public String getFormattedDateTime() {
        return formatter.format(date);
    }

    public int getYear() {
        Calendar instance = getCalendar();
        return instance.get(Calendar.YEAR);
    }

    private Calendar getCalendar() {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance;
    }

    public int getMonth() {
        Calendar instance = getCalendar();
        return instance.get(Calendar.MONTH) + 1;
    }

    public int getDay() {
        Calendar instance = getCalendar();
        return instance.get(Calendar.DAY_OF_MONTH);
    }

    public int getHours() {
        return getCalendar().get(Calendar.HOUR);
    }

    public int getMinutes() {
        return getCalendar().get(Calendar.MINUTE);
    }

    public int getSeconds() {
        return getCalendar().get(Calendar.SECOND);
    }


    public int getTimeZone() {
        return getCalendar().get(Calendar.ZONE_OFFSET);
    }
}
