package org.lbogdanov.poker.core;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.lbogdanov.poker.core.Duration.MINUTES_PER_DAY;
import static org.lbogdanov.poker.core.Duration.MINUTES_PER_HOUR;
import static org.lbogdanov.poker.core.Duration.MINUTES_PER_WEEK;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link Duration} class.
 * 
 * @author Alexandra Fomina
 *
 */
public class DurationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Test for {@link Duration#Duration(int)} with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgument() {
       new Duration(-1);
    }

    /**
     * Test for {@link Duration#parse(String)}.
     */
    @Test
    public void testParse() {
        Duration[] durations = {new Duration(30), new Duration(MINUTES_PER_HOUR),
                                new Duration(MINUTES_PER_DAY), new Duration(MINUTES_PER_WEEK)};

        assertArrayEquals(durations, Duration.parse("30m,1h,1d,1w").toArray());
        assertArrayEquals(durations, Duration.parse("30m 1h 1d 1w").toArray());
        assertArrayEquals(durations, Duration.parse("30m;1h;1d;1w").toArray());
    }

    /**
     * Test for {@link Duration#parse(String)} with invalid input.
     */
    @Test
    public void testParseWithInvalidInput1() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("q");
        Duration.parse("qwerty");
    }

    /**
     * Test for {@link Duration#parse(String)} with invalid input.
     */
    @Test
    public void testParseWithInvalidInput2() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("12y");
        Duration.parse("12y 5w 3h");
    }

    /**
     * Test for {@link Duration#parse(String)} with invalid input.
     */
    @Test
    public void testParseWithInvalidInput3() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(".");
        Duration.parse("12d.5w.3h");
    }

    /**
     * Test for {@link Duration#parse(String)} with invalid input.
     */
    @Test
    public void testParseWithInvalidInput4() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("12");
        Duration.parse("2h 12");
    }

    /**
     * Test for {@link Duration#toString()}.
     */
    @Test
    public void testToString() {
        assertEquals("1w 1d 1h 1m", new Duration(MINUTES_PER_WEEK + MINUTES_PER_DAY + MINUTES_PER_HOUR + 1).toString());
        assertEquals("0", new Duration().toString());
        assertEquals("30m", new Duration(30).toString());
        assertEquals("1d", new Duration(MINUTES_PER_DAY).toString());
    }

}
