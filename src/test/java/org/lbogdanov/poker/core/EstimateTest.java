package org.lbogdanov.poker.core;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.lbogdanov.poker.core.Estimate.MINUTES_PER_DAY;
import static org.lbogdanov.poker.core.Estimate.MINUTES_PER_HOUR;
import static org.lbogdanov.poker.core.Estimate.MINUTES_PER_WEEK;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link Estimate} class.
 * 
 * @author Alexandra Fomina
 *
 */
public class EstimateTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Test for {@link Estimate#Estimate(int)} with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgument() {
       new Estimate(-1);
    }

    /**
     * Test for {@link Estimate#parse(String)}.
     */
    @Test
    public void testParse() {
        Estimate[] Estimates = {new Estimate(30), new Estimate(MINUTES_PER_HOUR),
                                new Estimate(MINUTES_PER_DAY), new Estimate(MINUTES_PER_WEEK)};

        assertArrayEquals(Estimates, Estimate.parse("30m,1h,1d,1w").toArray());
        assertArrayEquals(Estimates, Estimate.parse("30m 1h 1d 1w").toArray());
        assertArrayEquals(Estimates, Estimate.parse("30m;1h;1d;1w").toArray());
    }

    /**
     * Test for {@link Estimate#parse(String)} with invalid input.
     */
    @Test
    public void testParseWithInvalidInput1() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("q");
        Estimate.parse("qwerty");
    }

    /**
     * Test for {@link Estimate#parse(String)} with invalid input.
     */
    @Test
    public void testParseWithInvalidInput2() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("12y");
        Estimate.parse("12y 5w 3h");
    }

    /**
     * Test for {@link Estimate#parse(String)} with invalid input.
     */
    @Test
    public void testParseWithInvalidInput3() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(".");
        Estimate.parse("12d.5w.3h");
    }

    /**
     * Test for {@link Estimate#parse(String)} with invalid input.
     */
    @Test
    public void testParseWithInvalidInput4() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("12");
        Estimate.parse("2h 12");
    }

    /**
     * Test for {@link Estimate#toString()}.
     */
    @Test
    public void testToString() {
        assertEquals("1w 1d 1h 1m", new Estimate(MINUTES_PER_WEEK + MINUTES_PER_DAY + MINUTES_PER_HOUR + 1).toString());
        assertEquals("0", new Estimate().toString());
        assertEquals("30m", new Estimate(30).toString());
        assertEquals("1d", new Estimate(MINUTES_PER_DAY).toString());
    }

}
