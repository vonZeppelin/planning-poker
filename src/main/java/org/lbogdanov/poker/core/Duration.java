/**
 * Copyright 2012 Leonid Bogdanov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lbogdanov.poker.core;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;


/**
 * Represents a time interval stored as a number of minutes, is used to represent an estimate in a Planning Poker game.
 * 
 * @author Alexandra Fomina
 */
public class Duration implements Comparable<Duration> {

    static final int MINUTES_PER_HOUR = 60;
    static final int MINUTES_PER_DAY = MINUTES_PER_HOUR * 8;
    static final int MINUTES_PER_WEEK = MINUTES_PER_DAY * 5;

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
        List<Duration> durations = new ArrayList<Duration>();
        StringBuilder duration = new StringBuilder();
        for (char chr : input.toCharArray()) {
            if (Character.isWhitespace(chr) || chr == ',' || chr == ';') {
                continue;
            }
            if (Character.isDigit(chr)) {
                duration.append(chr);
            } else {
                int mul;
                switch (chr) {
                    case 'm':
                        mul = 1;
                        break;
                    case 'h':
                        mul = MINUTES_PER_HOUR;
                        break;
                    case 'd':
                        mul = MINUTES_PER_DAY;
                        break;
                    case 'w':
                        mul = MINUTES_PER_WEEK;
                        break;
                   default:
                       throw new IllegalArgumentException(duration.toString() + chr);
                }
                durations.add(new Duration(Integer.parseInt(duration.toString()) * mul));
                duration.setLength(0);
            }
        }
        if (duration.length() > 0) {
            throw new IllegalArgumentException(duration.toString());
        }
        return durations;
    }

    /**
     * Creates a zero valued <code>Duration</code> instance.
     */
    public Duration() {
        this(0);
    }

    /**
     * Creates a <code>Duration</code> instance with a specified number of minutes.
     * 
     * @param minutes the initial number of minutes, must be non-negative
     */
    public Duration(int minutes) {
        setMinutes(minutes);
    }

    /**
     * Returns a number of minutes in this time interval.
     * 
     * @return the minutes the number of minutes
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * Sets a number of minutes that this time interval has.
     * 
     * @param minutes the number of minutes, must be non-negative
     */
    public void setMinutes(int minutes) {
        Preconditions.checkArgument(minutes >= 0, "Value must be non-negative");
        this.minutes = minutes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        int n = minutes;
        if (n == 0) {
            return "0";
        }
        List<String> duration = new ArrayList<String>(4);
        if (n >= MINUTES_PER_WEEK) {
            duration.add((n / MINUTES_PER_WEEK) + "w");
            n %= MINUTES_PER_WEEK;
        }
        if (n >= MINUTES_PER_DAY) {
            duration.add((n / MINUTES_PER_DAY) + "d");
            n %= MINUTES_PER_DAY;
        }
        if (n >= MINUTES_PER_HOUR) {
            duration.add((n / MINUTES_PER_HOUR) + "h");
            n %= MINUTES_PER_HOUR;
        }
        if (n > 0) {
            duration.add(n + "m");
        }
        return Joiner.on(' ').join(duration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Duration that) {
        return Ints.compare(this.minutes, that.minutes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof Duration) {
            return this.getMinutes() == ((Duration) other).getMinutes();
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return getMinutes();
    }

}
