package org.semanticweb.owlapi.sparql.builtin;

import com.google.common.base.Optional;

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
            return Optional.absent();
        }
    }

    public String getFormattedDateTime() {
        return formatter.format(date);
    }

    public int getYear() {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance.get(Calendar.YEAR);
    }

    public int getMonth() {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance.get(Calendar.MONTH);
    }

    public int getDay() {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance.get(Calendar.DAY_OF_MONTH);
    }
}
