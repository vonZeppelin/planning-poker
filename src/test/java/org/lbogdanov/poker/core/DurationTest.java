package org.lbogdanov.poker.core;


import static org.junit.Assert.assertEquals;
import static org.lbogdanov.poker.core.Duration.MINUTES_PER_DAY;
import static org.lbogdanov.poker.core.Duration.MINUTES_PER_HOUR;
import static org.lbogdanov.poker.core.Duration.MINUTES_PER_WEEK;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Tests for {@link Duration} class.
 * 
 * @author Alexandra Fomina
 *
 */
public class DurationTest {

    /**
     * Test for {@link Duration#parse(String)}
     */
    @Test
    public void testParse() {
        List<Duration> durations = Arrays.asList(new Duration(30), new Duration(MINUTES_PER_HOUR), 
                                    new Duration(MINUTES_PER_DAY), new Duration(MINUTES_PER_WEEK));
        assertEquals(durations, Duration.parse("30m,1h,1d,1w"));
        assertEquals(durations, Duration.parse("30m 1h 1d 1w"));
        assertEquals(durations, Duration.parse("30m;1h;1d;1w"));
        assertEquals(durations, Duration.parse("30m   1h    1d     1w "));
        assertEquals(durations, Duration.parse("30m , 1h ,  1d  , 1w"));
    }

    /**
     * Test for {@link Duration#parseSingle(String)}
     */
    @Test
    public void testParseSingle() {
        assertEquals(new Duration(), Duration.parseSingle("0d"));
        assertEquals(new Duration(2 * MINUTES_PER_WEEK), Duration.parseSingle("2w"));
    }

    /**
     * Test for {@link Duration#parse(String)} with invalid input
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidInput() {
        Duration.parse("qwerty");
    }

    /**
     * Test for {@link Duration#parse(String)} with invalid input
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidInput1() {
        Duration.parse("12y 5w 3h");
    }

    /**
     * Test for {@link Duration#toString()}
     */
    @Test
    public void testToString() {
        assertEquals("1w 1d 1h 1m", new Duration(MINUTES_PER_WEEK + MINUTES_PER_DAY + MINUTES_PER_HOUR + 1).toString());
        assertEquals("0", new Duration().toString());
        assertEquals("30m", new Duration(30).toString());
        assertEquals("1d 1m", new Duration(MINUTES_PER_DAY + 1).toString());
    }

}
