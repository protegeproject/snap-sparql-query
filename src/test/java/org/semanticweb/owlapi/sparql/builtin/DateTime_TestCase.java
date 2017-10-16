package org.semanticweb.owlapi.sparql.builtin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 16 Oct 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class DateTime_TestCase {

    private final String dateTime = "2011-01-10T14:45:13.815-05:00";

    @Test
    public void shouldParseZonedDateTime() throws Exception {
        Optional<DateTime> ts = DateTime.parseDateTime(dateTime);
        assertThat(ts.isPresent(), is(true));
    }

    @Test
    public void shouldParseUtcZonedDateTime() throws Exception {
        Optional<DateTime> ts = DateTime.parseDateTime("2011-01-10T14:45:13.815Z");
        assertThat(ts.isPresent(), is(true));
    }

    @Test
    public void shouldFormatDatetime() {
        DateTime ts = parse();
        assertThat(ts.getFormattedDateTime(), is(dateTime));
    }

    @SuppressWarnings("ConstantConditions")
    private DateTime parse() {
        Optional<DateTime> timestamp = DateTime.parseDateTime(dateTime);
        return timestamp.orElseThrow(() -> new RuntimeException("Could not parse datetime"));
    }

    @Test
    public void shouldGetYear() {
        DateTime dateTime = parse();
        assertThat(dateTime.getYear(), is(2011));
    }

    @Test
    public void shouldGetMonth() {
        DateTime dateTime = parse();
        assertThat(dateTime.getMonth(), is(1));
    }

    @Test
    public void shouldGetDay() {
        DateTime dateTime = parse();
        assertThat(dateTime.getDay(), is(10));
    }

    @Test
    public void shouldGetHour() {
        DateTime dateTime = parse();
        assertThat(dateTime.getHour(), is(14));
    }

    @Test
    public void shouldGetMinute() {
        DateTime dateTime = parse();
        assertThat(dateTime.getMinute(), is(45));
    }

    @Test
    public void shouldGetSeconds() {
        DateTime dateTime = parse();
        assertThat(dateTime.getSeconds(), is(13.815));
    }
}
