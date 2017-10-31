package org.semanticweb.owlapi.sparql.builtin;

import com.google.common.base.MoreObjects;

import javax.annotation.Nonnull;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import static com.google.common.base.Objects.toStringHelper;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 06/06/15
 */
public class DateTime {

    private final ZonedDateTime zonedDateTime;

    public DateTime(@Nonnull ZonedDateTime zonedDateTime) {
        this.zonedDateTime = zonedDateTime;
    }

    public long getEpochMilli() {
        return zonedDateTime.toInstant().toEpochMilli();
    }

    public static Optional<DateTime> parseDateTime(@Nonnull String dateTime) {
        try {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTime);
            return Optional.of(new DateTime(zonedDateTime));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    public String getFormattedDateTime() {
        return zonedDateTime.toString();
    }

    public int getYear() {
        return zonedDateTime.getYear();
    }

    public int getMonth() {
        return zonedDateTime.getMonthValue();
    }

    public int getDay() {
        return zonedDateTime.getDayOfMonth();
    }

    public int getHour() {
        return zonedDateTime.getHour();
    }

    public int getMinute() {
        return zonedDateTime.getMinute();
    }

    /**
     * Gets the seconds and fraction of seconds from this DateTime.  Note that this is not the same
     * as {@link ZonedDateTime#getSecond()}, which returns whole seconds.
     * @return The seconds and fractions of seconds.
     */
    public double getSeconds() {
        double fractionSeconds = zonedDateTime.getNano() / 1000.0 / 1000.0 / 1000.0;
        return zonedDateTime.getSecond() + fractionSeconds;
    }

    public String getTz() {
        return zonedDateTime.getZone().getId();
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("DateTime")
                .addValue(zonedDateTime)
                .toString();
    }
}
