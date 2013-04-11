package org.lbogdanov.poker.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;

/**
 * Represents time duration stored as a number of minutes.
 * 
 * @author Alexandra Fomina
 */
public class Duration implements Serializable, Comparable<Duration> {

    private static final int MINUTES_PER_HOUR = 60;
    private static final int MINUTES_PER_DAY = MINUTES_PER_HOUR * 8;
    private static final int MINUTES_PER_WEEK = MINUTES_PER_DAY * 5;

    private int minutes;

    /**
     * Parses the given <code>String</code> to a <code>List</code> of <code>Duration</code> objects.
     * The <code>String</code> represents duration values in minutes, hours, days or weeks,
     * separated with spaces, commas or semicolons. For example "30m 4h 1d 2w",
     * where 'm' stands for minute, 'h' - hour, 'd' - day and 'w' - week.
     * 
     * @param input a <code>String</code> to parse
     * @return the durations represented by the given <code>String</code>
     * @throws IllegalArgumentException if the given <code>String</code> doesn't matches syntax
     */
    public static List<Duration> parse(String input) {
        if (!Pattern.matches("(\\d+[mhdw]\\s*[\\s,;]?\\s*)*", input)) {
            throw new IllegalArgumentException("Incorrect input");
        }
        List<Duration> durations = new ArrayList<Duration>();
        for (String d : input.split("\\s*[\\s,;]\\s*")) {
            durations.add(parseSingle(d));
        }
        return durations;
    }

    /**
     * Parses the given <code>String</code> to a single <code>Duration</code> object.
     * 
     * @param input a string to parse
     * @return the duration
     */
    public static Duration parseSingle(String input) {
        Duration duration = new Duration();
        Pattern pattern = Pattern.compile("(\\d+)([mhdw])");
        Matcher matcher = pattern.matcher(input);
        int n = Integer.parseInt(matcher.group(1));
        switch (matcher.group(2).charAt(0)) {
            case 'm':
                break;
            case 'h':
                n *= MINUTES_PER_HOUR;
                break;
            case 'd':
                n *= MINUTES_PER_DAY;
                break;
            case 'w':
                n *= MINUTES_PER_WEEK;
                break;
        }
        duration.setMinutes(n);
        return duration;
    }

    public Duration() {}

    /**
     * @param minutes
     */
    public Duration(int minutes) {
        this.minutes = minutes;
    }

    /**
     * @return the minutes
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * @param minutes the minutes to set
     */
    public void setMinutes(int minutes) {
        Preconditions.checkArgument(minutes >= 0, "Argument must be non-negative");
        this.minutes = minutes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        if (minutes % MINUTES_PER_WEEK == 0) {
            return String.format("%dw", minutes / MINUTES_PER_WEEK);
        }
        if (minutes % MINUTES_PER_DAY == 0) {
            return String.format("%dd", minutes / MINUTES_PER_DAY);
        }
        if (minutes % MINUTES_PER_HOUR == 0) {
            return String.format("%dh", minutes / MINUTES_PER_HOUR);
        }
        return String.format("%dm", minutes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Duration that) {
        return Ints.compare(this.minutes, that.minutes);
    }

}
